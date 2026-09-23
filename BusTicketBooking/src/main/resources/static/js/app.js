// BusGo UI Logic
const API_BASE = '';

let currentBuses = [];
let selectedBus = null;
let selectedSeat = null;

// Set default travel date to tomorrow
document.addEventListener('DOMContentLoaded', () => {
  const tomorrow = new Date();
  tomorrow.setDate(tomorrow.getDate() + 1);
  document.getElementById('travelDate').valueAsDate = tomorrow;

  // Nav link handling
  document.querySelectorAll('.nav-links li').forEach(link => {
    link.addEventListener('click', () => {
      const target = link.getAttribute('data-target');
      showSection(target);
    });
  });

  // Admin tab handling
  document.querySelectorAll('.admin-tab').forEach(tab => {
    tab.addEventListener('click', () => {
      document.querySelectorAll('.admin-tab').forEach(t => t.classList.remove('active'));
      tab.classList.add('active');
      const tabName = tab.getAttribute('data-admin');
      if (tabName === 'buses') loadAdminBuses();
      else renderAdminPlaceholder(tabName);
    });
  });

  // Sort handling
  document.getElementById('sortSelect').addEventListener('change', (e) => {
    sortBuses(e.target.value);
  });
});

function showSection(sectionId) {
  document.querySelectorAll('.section').forEach(sec => sec.classList.add('hidden'));
  const target = document.getElementById(sectionId);
  if (target) target.classList.remove('hidden');

  document.querySelectorAll('.nav-links li').forEach(li => li.classList.remove('active'));
  const activeLink = document.querySelector(`.nav-links li[data-target="${sectionId}"]`);
  if (activeLink) activeLink.classList.add('active');

  if (sectionId === 'admin') loadAdminBuses();
  window.scrollTo({ top: 0, behavior: 'smooth' });
}

function quickSearch(source, destination) {
  document.getElementById('source').value = source;
  document.getElementById('destination').value = destination;
  searchBuses();
}

async function searchBuses() {
  const source = document.getElementById('source').value.trim();
  const destination = document.getElementById('destination').value.trim();

  if (!source || !destination) {
    showToast('Please enter both source and destination', 'error');
    return;
  }

  showSection('results');
  const resultsContainer = document.getElementById('busResults');
  resultsContainer.innerHTML = '<div class="empty-state"><p>Searching buses...</p></div>';
  document.getElementById('resultsHeading').textContent = 'Searching...';

  try {
    const url = `${API_BASE}/allBusses/getBussesBy?source=${encodeURIComponent(source)}&destination=${encodeURIComponent(destination)}`;
    const response = await fetch(url);
    if (!response.ok) throw new Error('Failed to fetch buses');
    const buses = await response.json();
    currentBuses = buses || [];
    renderBuses(currentBuses);
  } catch (error) {
    console.error(error);
    resultsContainer.innerHTML = `
      <div class="empty-state">
        <p>Could not load buses. Make sure the Spring Boot app is running.</p>
        <p style="font-size:13px; margin-top:10px;">${error.message}</p>
      </div>`;
    document.getElementById('resultsHeading').textContent = '0 Buses Found';
  }
}

function sortBuses(criteria) {
  let sorted = [...currentBuses];
  if (criteria === 'price') {
    sorted.sort((a, b) => (a.price || 0) - (b.price || 0));
  } else if (criteria === 'departure') {
    sorted.sort((a, b) => (a.time || '').localeCompare(b.time || ''));
  }
  renderBuses(sorted);
}

function renderBuses(buses) {
  const container = document.getElementById('busResults');
  document.getElementById('resultsHeading').textContent = `${buses.length} Bus${buses.length !== 1 ? 'es' : ''} Found`;

  if (buses.length === 0) {
    container.innerHTML = `
      <div class="empty-state">
        <p>No buses found for this route.</p>
        <p style="font-size:13px; margin-top:10px;">Try a different source or destination.</p>
      </div>`;
    return;
  }

  container.innerHTML = buses.map(bus => `
    <div class="bus-card">
      <div class="bus-info">
        <h3>${escapeHtml(bus.busName || 'Bus')}</h3>
        <span class="bus-type">${bus.seats_available > 20 ? 'AC Sleeper' : 'AC Seater'}</span>
        <div class="bus-route">
          <span class="time">${formatTime(bus.time)}</span>
          <span>→</span>
          <span class="time">${formatArrival(bus.time)}</span>
          <span class="duration">${bus.time || '-'}</span>
        </div>
        <div class="bus-route" style="margin-top:6px; font-size:14px;">
          ${escapeHtml(bus.source || '')} → ${escapeHtml(bus.destination || '')}
        </div>
        <div class="amenities">
          <span class="amenity">📶 WiFi</span>
          <span class="amenity">🔌 Charging</span>
          <span class="amenity">Bus No: ${escapeHtml(bus.busNumber || '-')}</span>
        </div>
      </div>
      <div class="bus-price">
        <div class="seats-left">${bus.seats_available || 0} seats left</div>
        <div class="price">₹${bus.price || 0} <span>/person</span></div>
      </div>
      <button class="btn btn-primary" onclick="selectBus(${bus.b_ID})">View Seats</button>
    </div>
  `).join('');
}

function selectBus(busId) {
  selectedBus = currentBuses.find(b => b.b_ID === busId);
  if (!selectedBus) return;

  selectedSeat = generateSeatNumber();

  document.getElementById('summaryBusName').textContent = selectedBus.busName || 'Bus';
  document.getElementById('summaryRoute').textContent = `${selectedBus.source} → ${selectedBus.destination}`;
  document.getElementById('summaryTime').textContent = `${formatTime(selectedBus.time)}, ${getTravelDate()}`;
  document.getElementById('summarySeat').textContent = selectedSeat;
  document.getElementById('summaryFare').textContent = `₹${selectedBus.price || 0}`;
  document.getElementById('summaryTotal').textContent = `₹${selectedBus.price || 0}`;

  document.getElementById('bookingForm').reset();
  showSection('booking');
}

async function submitBooking(event) {
  event.preventDefault();
  if (!selectedBus) return;

  const name = document.getElementById('pName').value.trim();
  const age = parseInt(document.getElementById('pAge').value);
  const email = document.getElementById('pEmail').value.trim();
  const phone = document.getElementById('pPhone').value.trim();
  const gender = document.querySelector('input[name="gender"]:checked')?.value;

  const payload = {
    p_Name: name,
    age: age,
    busNumber: selectedBus.busNumber,
    source: selectedBus.source,
    destination: selectedBus.destination,
    price: selectedBus.price,
    seats: 1,
    ticketNumber: generateTicketNumber()
  };

  const submitBtn = event.target.querySelector('button[type="submit"]');
  const originalText = submitBtn.textContent;
  submitBtn.disabled = true;
  submitBtn.innerHTML = '<span class="loading"></span> Booking...';

  try {
    const response = await fetch(`${API_BASE}/bookTicket/bookBusTickets`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });

    if (!response.ok) throw new Error('Booking failed');
    const result = await response.json();
    showToast(`Booking confirmed! Ticket: ${result.ticketNumber || payload.ticketNumber}`, 'success');
    setTimeout(() => showSection('home'), 2000);
  } catch (error) {
    console.error(error);
    showToast('Booking failed. Please try again.', 'error');
  } finally {
    submitBtn.disabled = false;
    submitBtn.textContent = originalText;
  }
}

async function loadAdminBuses() {
  const tableBody = document.getElementById('adminBusTable');
  tableBody.innerHTML = '<tr><td colspan="6" class="empty-cell">Loading buses...</td></tr>';

  try {
    const response = await fetch(`${API_BASE}/allBusses/getAllBusses`);
    if (!response.ok) throw new Error('Failed to load buses');
    const buses = await response.json();

    if (!buses || buses.length === 0) {
      tableBody.innerHTML = '<tr><td colspan="6" class="empty-cell">No buses found in database.</td></tr>';
      return;
    }

    tableBody.innerHTML = buses.map(bus => `
      <tr>
        <td>${escapeHtml(bus.busNumber || '-')}</td>
        <td>${escapeHtml(bus.busName || '-')}</td>
        <td>${escapeHtml(bus.source || '')} → ${escapeHtml(bus.destination || '')}</td>
        <td>${bus.seats_available || 0}</td>
        <td><span class="status active">Active</span></td>
        <td class="action-btns">
          <button class="icon-btn" title="Edit">✎</button>
          <button class="icon-btn" title="Delete">🗑</button>
        </td>
      </tr>
    `).join('');
  } catch (error) {
    console.error(error);
    tableBody.innerHTML = `
      <tr>
        <td colspan="6" class="empty-cell">
          Could not load buses. Make sure the Spring Boot app is running.
        </td>
      </tr>`;
  }
}

function renderAdminPlaceholder(tabName) {
  const tableBody = document.getElementById('adminBusTable');
  tableBody.innerHTML = `
    <tr>
      <td colspan="6" class="empty-cell">
        ${tabName.charAt(0).toUpperCase() + tabName.slice(1)} management view would appear here.
      </td>
    </tr>`;
}

// Helpers
function formatTime(timeStr) {
  if (!timeStr) return '--:--';
  const parts = timeStr.split(':');
  if (parts.length >= 2) return `${parts[0]}:${parts[1]}`;
  return timeStr;
}

function formatArrival(timeStr) {
  if (!timeStr) return '--:--';
  const parts = timeStr.split(':');
  if (parts.length >= 2) {
    let hours = parseInt(parts[0], 10);
    let minutes = parseInt(parts[1], 10);
    hours = (hours + 8) % 24;
    return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}`;
  }
  return timeStr;
}

function getTravelDate() {
  const input = document.getElementById('travelDate').value;
  if (!input) return 'Today';
  const date = new Date(input);
  return date.toLocaleDateString('en-IN', { day: 'numeric', month: 'short' });
}

function generateSeatNumber() {
  const row = String.fromCharCode(65 + Math.floor(Math.random() * 6));
  const num = Math.floor(Math.random() * 10) + 1;
  return `${row}${num}`;
}

function generateTicketNumber() {
  return 'TKT' + Math.floor(100000 + Math.random() * 900000);
}

function escapeHtml(text) {
  if (text == null) return '';
  return String(text)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#039;');
}

function showToast(message, type = '') {
  const toast = document.getElementById('toast');
  toast.textContent = message;
  toast.className = 'toast show ' + type;
  setTimeout(() => {
    toast.classList.remove('show');
  }, 3000);
}
