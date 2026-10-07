'use strict';
const fmt = n => n.toLocaleString('vi-VN') + 'đ';
const $ = (s, r = document) => r.querySelector(s);
const $$ = (s, r = document) => [...r.querySelectorAll(s)];
const reduce = matchMedia('(prefers-reduced-motion: reduce)').matches;
const img = (id, w = 600) => `https://images.unsplash.com/${id}?auto=format&fit=crop&w=${w}&q=80`;

const MOVIES = [
  { id: 'ds', t: 'Demon Slayer', sub: 'Kimetsu no Yaiba: Chuyến Tàu Vô Tận', p: 'photo-1534447677768-be436bb09401', bd: 'photo-1536440136628-849c177e76a1', age: 'T16', r: 8.9, d: '2h 10m', g: 'Anime · Hành động', dir: 'Haruo Sotozaki', cast: 'Natsuki Hanae, Akari Kitō', fm: ['IMAX', '2D'], st: 'now', desc: 'Tanjirou và các đồng đội lên Chuyến tàu Vô Tận để điều tra vụ hơn 40 người mất tích bí ẩn.' },
  { id: 'dn', t: 'Dune: Hành Tinh Cát II', sub: 'Cuộc chiến giành Arrakis', p: 'photo-1518709268805-4e9042af9f23', bd: 'photo-1518709268805-4e9042af9f23', age: 'T13', r: 8.7, d: '2h 45m', g: 'Khoa học viễn tưởng', dir: 'Denis Villeneuve', cast: 'Timothée Chalamet, Zendaya', fm: ['IMAX', '3D'], st: 'now', desc: 'Paul Atreides hợp lực cùng người Fremen để trả thù những kẻ đã hủy hoại gia tộc mình.' },
  { id: 'kf', t: 'Kung Fu Panda 4', sub: 'Gấu Po trở lại', p: 'photo-1509198397868-475647b2a1e5', bd: 'photo-1509198397868-475647b2a1e5', age: 'P', r: 9.1, d: '1h 55m', g: 'Hoạt hình · Hài hước', dir: 'Mike Mitchell', cast: 'Jack Black, Awkwafina', fm: ['2D', '4DX'], st: 'now', desc: 'Po phải chọn người kế vị Chiến binh Rồng khi một phù thủy biến hình xuất hiện.' },
  { id: 'it', t: 'Interstellar (chiếu lại)', sub: 'Phiên bản IMAX', p: 'photo-1618005182384-a83a8bd57fbe', bd: 'photo-1618005182384-a83a8bd57fbe', age: 'T18', r: 9.3, d: '2h 49m', g: 'Viễn tưởng · Phiêu lưu', dir: 'Christopher Nolan', cast: 'Matthew McConaughey, Anne Hathaway', fm: ['IMAX'], st: 'now', desc: 'Một nhóm nhà thám hiểm băng qua hố sâu không gian để tìm ngôi nhà mới cho nhân loại.' },
  { id: 'mi', t: 'Mai', sub: 'Phim tâm lý tình cảm Việt', p: 'photo-1489599849927-2ee91cede3ba', bd: 'photo-1489599849927-2ee91cede3ba', age: 'T18', r: 8.2, d: '2h 11m', g: 'Tâm lý · Tình cảm', dir: 'Trấn Thành', cast: 'Phương Anh Đào, Tuấn Trần', fm: ['2D'], st: 'now', desc: 'Câu chuyện về người phụ nữ tên Mai và cơ hội thứ hai trong tình yêu.' },
  { id: 'gk', t: 'Godzilla x Kong', sub: 'Đế chế mới', p: 'photo-1478720568477-152d9b164e26', bd: 'photo-1478720568477-152d9b164e26', age: 'T13', r: 8.0, d: '1h 55m', g: 'Hành động · Quái vật', dir: 'Adam Wingard', cast: 'Rebecca Hall, Dan Stevens', fm: ['4DX', '3D'], st: 'soon', desc: 'Hai người khổng lồ phải liên minh trước một mối đe dọa ẩn sâu dưới lòng đất.' }
];
const CINEMAS = [
  { id: 'v', n: 'Vincom Center Q1', a: '72 Lê Thánh Tôn, Quận 1', rooms: 8, fm: ['IMAX', '2D', '3D'], room: 'Phòng IMAX Laser' },
  { id: 'l', n: 'Landmark 81', a: '720A Điện Biên Phủ, Bình Thạnh', rooms: 6, fm: ['4DX', '2D'], room: 'Phòng 4DX' },
  { id: 'a', n: 'Aeon Tân Phú', a: '30 Bờ Bao Tân Thắng, Tân Phú', rooms: 7, fm: ['2D', '3D'], room: 'Phòng 3D' }
];
const TIMES = ['10:00', '13:30', '16:15', '19:30', '21:45'];
const COMBOS = [
  { id: 'duo', n: 'Combo Duo Supreme', d: '1 bắp lớn phô mai, 2 nước 32oz, 1 snack khoai tây', p: 109000, i: 'photo-1578849278619-e73505e9610f' },
  { id: 'solo', n: 'Combo Solo Cinema', d: '1 bắp lớn bơ rang, 1 nước 32oz chọn vị', p: 79000, i: 'photo-1585647347483-22b66260dfff' }
];
const PROMOS = {
  CINE2026: { t: 'Giảm 10% toàn bộ đơn', c: (s, c) => Math.round((s + c) * .1) },
  HSSV: { t: 'Giảm 20.000đ cho học sinh, sinh viên', c: (s, c) => Math.min(20000, s + c) },
  COMBO15: { t: 'Giảm 15% tiền bắp nước', c: (s, c) => Math.round(c * .15) }
};
const PRICE = { standard: 120000, vip: 150000, couple: 280000 };
const TYPE_NAME = { standard: 'Thường', vip: 'VIP', couple: 'Ghế đôi' };
const ROWS = ['A', 'B', 'C', 'D', 'E', 'F'];
const rowType = r => r === 'F' ? 'couple' : 'CD'.includes(r) ? 'vip' : 'standard';

const today = new Date();
const DATES = [0, 1, 2].map(i => { const d = new Date(today); d.setDate(d.getDate() + i); const dd = String(d.getDate()).padStart(2, '0'), mm = String(d.getMonth() + 1).padStart(2, '0'); return { k: `${dd}/${mm}`, l: (i ? ['', 'Ngày mai', 'Ngày kia'][i] : 'Hôm nay') + ` ${dd}/${mm}` }; });

const S = {
  show: { m: MOVIES[0], date: DATES[0].k, cin: CINEMAS[0], time: '19:30' },
  sel: [], combos: { duo: 0, solo: 0 }, promo: null, step: 1, ticker: 0, raf: 0,
  booked: {}, hold: { t: null, left: 600 },
  stDate: DATES[0].k, stCin: 'all', filter: 'all', q: '',
  pos: { show: null, sel: [], combos: { duo: 0, solo: 0 }, promo: '' }
};
const showKey = s => `${s.m.id}|${s.date}|${s.cin.id}|${s.time}`;
function soldFor(s) {
  const key = showKey(s); let h = 0; for (const ch of key) h = (h * 31 + ch.charCodeAt(0)) >>> 0;
  const set = new Set(S.booked[key] || []);
  for (let i = 0; i < 9; i++) { h = (h * 1103515245 + 12345) >>> 0; const r = ROWS[h % 6], max = r === 'F' ? 5 : 10; set.add(r + (1 + (h >> 8) % max)); }
  return set;
}
function totals(sel, combos, promo) {
  const seat = sel.reduce((a, x) => a + x.price, 0);
  const combo = COMBOS.reduce((a, c) => a + c.p * (combos[c.id] || 0), 0);
  const disc = promo && PROMOS[promo] ? Math.min(PROMOS[promo].c(seat, combo), seat + combo) : 0;
  return { seat, combo, disc, total: seat + combo - disc };
}
function toast(msg) {
  const t = $('#toast'); t.textContent = msg; t.classList.add('show');
  clearTimeout(toast.t); toast.t = setTimeout(() => t.classList.remove('show'), 2600);
}
const poster = m => `<img src="${img(m.p)}" alt="${m.t}" loading="lazy" onerror="this.classList.add('img-fail')">`;

/* ================= KHỞI TẠO ================= */
document.addEventListener('DOMContentLoaded', () => {
  initRoles(); initNav(); initHero(); initMovies(); initShowtimes(); initStatic();
  initModal(); initDetail(); initPOS(); initAdmin();
});

function initRoles() {
  const demo = $('#demo'), tg = $('#demo-toggle');
  tg.onclick = () => { const o = demo.classList.toggle('open'); tg.setAttribute('aria-expanded', o); };
  $$('.role-btn').forEach(b => b.onclick = () => {
    $$('.role-btn').forEach(x => x.classList.toggle('active', x === b));
    $$('.role-view').forEach(v => v.classList.toggle('active', v.id === 'view-' + b.dataset.role));
    document.documentElement.className = 'theme-' + b.dataset.role;
    closeModal(); closeDetail(); demo.classList.remove('open'); tg.setAttribute('aria-expanded', false); scrollTo(0, 0);
  });
  document.addEventListener('click', e => { if (!demo.contains(e.target)) demo.classList.remove('open'); });
}

function initNav() {
  const nav = $('.cinematic-nav');
  const links = $$('.nav-link'), secs = links.map(l => $(l.getAttribute('href')));
  const on = () => {
    nav.classList.toggle('scrolled', scrollY > 40);
    let cur = 0; secs.forEach((s, i) => { if (s && s.getBoundingClientRect().top < innerHeight * .4) cur = i; });
    links.forEach((l, i) => l.classList.toggle('active', i === cur));
  };
  addEventListener('scroll', on, { passive: true }); on();
}


const heroList = MOVIES.slice(0, 4);
let heroIdx = 0, heroToken = 0, heroTimer;
function initHero() {
  $('#hero-dots').innerHTML = heroList.map((m, i) => `<button class="dot" aria-label="${m.t}" data-i="${i}"></button>`).join('');
  $('#hero-dots').onclick = e => { const d = e.target.closest('.dot'); if (d) { showHero(+d.dataset.i); startHero(); } };
  $('#hero-book').onclick = () => openDetail(heroList[heroIdx], true);
  $('#hero-detail').onclick = () => openDetail(heroList[heroIdx]);
  $('#hero-trailer').onclick = () => toast('Trailer sẽ được cập nhật sớm');
  $('#hero-backdrop').style.backgroundImage = `url('${img(heroList[0].bd, 1920)}')`;
  fillHero(0); startHero();
}
function startHero() { clearInterval(heroTimer); if (!reduce) heroTimer = setInterval(() => showHero((heroIdx + 1) % heroList.length), 7000); }
function fillHero(i) {
  const m = heroList[i]; heroIdx = i;
  $('#hero-title').textContent = m.t; $('#hero-subtitle').textContent = m.sub; $('#hero-desc').textContent = m.desc;
  $('#hero-fmt').textContent = m.fm.join(' · '); renderHeroNext(m);
  $('#hero-meta').innerHTML = `<span class="meta-rating">★ ${m.r}</span><span>${m.d}</span><span class="badge-age">${m.age}</span><span>${m.g}</span>`;
  $$('#hero-dots .dot').forEach((d, k) => d.classList.toggle('active', k === i));
}
function showHero(i) {
  if (i === heroIdx) return;
  const token = ++heroToken, bg = $('#hero-backdrop'), box = $('#hero-content');
  const pre = new Image(); pre.src = img(heroList[i].bd, 1920);
  const go = () => {
    if (token !== heroToken) return;            // đã có yêu cầu mới hơn
    box.classList.add('fading'); bg.classList.add('fading');
    setTimeout(() => {
      if (token !== heroToken) return;
      bg.style.backgroundImage = `url('${pre.src}')`; fillHero(i);
      box.classList.remove('fading'); bg.classList.remove('fading');
    }, reduce ? 0 : 300);
  };
  pre.complete ? go() : (pre.onload = pre.onerror = go);
  $$('#hero-dots .dot').forEach((d, k) => d.classList.toggle('active', k === i));
}

const norm = s => s.toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g, '').replace(/đ/g, 'd');
function initMovies() {
  const grid = $('#movie-grid');
  grid.innerHTML = MOVIES.map(m => `
    <article class="cinema-poster-card" data-id="${m.id}" tabindex="0" role="button" aria-label="Đặt vé ${m.t}">
      <div class="poster-wrapper">${poster(m)}<div class="poster-overlay"></div>
        <div class="badge-corner">${m.age}</div>${m.st === 'soon' ? '<div class="badge-soon">Sắp chiếu</div>' : ''}
        <div class="poster-hover-actions"><button class="hv-btn" data-act="trailer">▶ Xem trailer</button><button class="hv-btn" data-act="detail">Xem chi tiết</button>${m.st === 'now' ? '<button class="hv-btn gold" data-act="book">Đặt vé</button>' : ''}</div></div>
      <div class="poster-info"><div class="meta-line"><span class="rating">★ ${m.r}</span><span>${m.d}</span><span>${m.fm.join(' / ')}</span></div>
        <h3 class="movie-name">${m.t}</h3><p class="genre">${m.g}</p></div>
    </article>`).join('');
  grid.onclick = e => { const c = e.target.closest('.cinema-poster-card'); if (!c) return; const m = MOVIES.find(x => x.id === c.dataset.id), a = e.target.closest('[data-act]')?.dataset.act; if (a === 'trailer') return toast('Trailer sẽ được cập nhật sớm'); openDetail(m, a === 'book'); };
  grid.onkeydown = e => { if (e.key === 'Enter' || e.key === ' ') { const c = e.target.closest('.cinema-poster-card'); if (c) { e.preventDefault(); openDetail(MOVIES.find(x => x.id === c.dataset.id)); } } };
  $('#filters').onclick = e => { const p = e.target.closest('.pill'); if (!p) return; S.filter = p.dataset.f; $$('#filters .pill').forEach(x => x.classList.toggle('active', x === p)); applyFilter(); };
  $('#search').oninput = e => { S.q = e.target.value; applyFilter(); if (S.q) $('#movies').scrollIntoView({ behavior: 'smooth' }); };
  $('#clear-search').onclick = () => { $('#search').value = ''; S.q = ''; S.filter = 'all'; $$('#filters .pill').forEach(x => x.classList.toggle('active', x.dataset.f === 'all')); applyFilter(); };
  applyFilter();
}
function pickMovie(id) {
  const m = MOVIES.find(x => x.id === id);
  if (m.st === 'soon') return toast(`${m.t} chưa mở bán vé`);
  $('#showtimes').scrollIntoView({ behavior: 'smooth' }); toast(`Chọn suất chiếu cho ${m.t}`); openBooking(m);
}
function matches(m) {
  const f = S.filter, q = norm(S.q.trim());
  const okF = f === 'all' || m.st === f || m.fm.includes(f);
  const okQ = !q || norm([m.t, m.sub, m.dir, m.cast, m.g, m.fm.join(' '), m.st === 'now' ? 'dang chieu' : 'sap chieu'].join(' ')).includes(q);
  return m.st !== 'off' && okF && okQ;
}
function applyFilter() {
  const cards = $$('.cinema-poster-card'); let n = 0;
  cards.forEach(c => {
    const m = MOVIES.find(x => x.id === c.dataset.id), ok = matches(m);
    if (ok) { n++; c.hidden = false; requestAnimationFrame(() => c.classList.remove('out')); }
    else { c.classList.add('out'); setTimeout(() => { if (c.classList.contains('out')) c.hidden = true; }, reduce ? 0 : 250); }
  });
  $('#empty-state').hidden = n > 0;
  $('#movie-count').textContent = n ? `${n} phim` : '';
}

function initShowtimes() {
  $('#st-dates').innerHTML = DATES.map((d, i) => `<button class="pill${i ? '' : ' active'}" data-k="${d.k}">${d.l}</button>`).join('');
  $('#st-cinemas').innerHTML = `<button class="pill active" data-c="all">Tất cả rạp</button>` + CINEMAS.map(c => `<button class="pill" data-c="${c.id}">${c.n}</button>`).join('');
  $('#st-dates').onclick = e => { const p = e.target.closest('.pill'); if (!p) return; S.stDate = p.dataset.k; $$('#st-dates .pill').forEach(x => x.classList.toggle('active', x === p)); renderShowtimes(); };
  $('#st-cinemas').onclick = e => { const p = e.target.closest('.pill'); if (!p) return; S.stCin = p.dataset.c; $$('#st-cinemas .pill').forEach(x => x.classList.toggle('active', x === p)); renderShowtimes(); };
  $('#st-list').onclick = e => {
    const b = e.target.closest('.time-chip'); if (!b || b.disabled) return;
    openBooking(MOVIES.find(m => m.id === b.dataset.m), { cin: CINEMAS.find(c => c.id === b.dataset.c), time: b.dataset.t, date: S.stDate });
  };
  renderShowtimes();
}
function renderShowtimes() {
  const now = new Date(), isToday = S.stDate === DATES[0].k, nowStr = `${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`;
  const cins = CINEMAS.filter(c => S.stCin === 'all' || c.id === S.stCin);
  $('#st-list').innerHTML = MOVIES.filter(m => m.st === 'now').map(m => {
    const rows = cins.filter(c => c.fm.some(f => m.fm.includes(f))).map(c => {
      const times = TIMES.map((t, i) => ((i + c.id.charCodeAt(0)) % 5 === 4) ? '' : `<button class="time-chip" data-m="${m.id}" data-c="${c.id}" data-t="${t}" ${isToday && t < nowStr ? 'disabled' : ''}>${t}</button>`).join('');
      return `<div class="st-cin"><span class="st-cin-name">${c.n}</span><div class="st-times">${times}</div></div>`;
    }).join('');
    return `<div class="st-row"><div class="st-movie">${poster(m)}<div><h3>${m.t}</h3><p>${m.d} · ${m.fm.join(' / ')} · ${m.age}</p></div></div><div class="st-cins">${rows || '<p class="muted">Không có suất ở rạp này.</p>'}</div></div>`;
  }).join('');
}
function initStatic() {
  $('#cinema-grid').innerHTML = CINEMAS.map(c => `<article class="cinema-card"><h3>${c.n}</h3><p>${c.a}</p><div class="cin-meta"><span>${c.rooms} phòng chiếu</span>${c.fm.map(f => `<span class="tag">${f}</span>`).join('')}</div><button class="btn-glass" data-c="${c.id}">Xem lịch chiếu</button></article>`).join('');
  $('#cinema-grid').onclick = e => { const b = e.target.closest('[data-c]'); if (!b) return; S.stCin = b.dataset.c; $$('#st-cinemas .pill').forEach(x => x.classList.toggle('active', x.dataset.c === S.stCin)); renderShowtimes(); $('#showtimes').scrollIntoView({ behavior: 'smooth' }); };
  $('#promo-grid').innerHTML = Object.entries(PROMOS).map(([k, p]) => `<button class="promo-card" data-code="${k}"><strong>${k}</strong><span>${p.t}</span><em>Sao chép mã</em></button>`).join('');
  $('#promo-grid').onclick = e => { const b = e.target.closest('.promo-card'); if (!b) return; navigator.clipboard?.writeText(b.dataset.code).catch(() => {}); toast(`Đã sao chép mã ${b.dataset.code}`); };
}

function drawSeats(el, sold, selected, onToggle) {
  el.innerHTML = ROWS.map(r => {
    const t = rowType(r), n = t === 'couple' ? 5 : 10; let h = '';
    for (let i = 1; i <= n; i++) {
      const id = r + i, isSold = sold.has(id), isSel = selected.some(s => s.id === id);
      h += `<button type="button" class="seat-node ${t}${isSold ? ' sold' : ''}${isSel ? ' selected' : ''}${i === n / 2 | 0 && t !== 'couple' && i === 5 ? ' aisle' : ''}${t === 'couple' && i === 3 ? ' aisle' : ''}" data-id="${id}" data-t="${t}" data-tip="${id} · ${TYPE_NAME[t]} · ${fmt(PRICE[t])}" aria-label="Ghế ${id}, ${TYPE_NAME[t]}, ${fmt(PRICE[t])}${isSold ? ', đã bán' : ''}" aria-pressed="${isSel}" ${isSold ? 'disabled' : ''}>${t === 'couple' ? '<b>♥</b>' : ''}</button>`;
    }
    return `<div class="seat-row"><span class="row-label">${r}</span>${h}<span class="row-label">${r}</span></div>`;
  }).join('');
  el.onclick = e => {
    const b = e.target.closest('.seat-node'); if (!b || b.disabled) return;
    const id = b.dataset.id, i = selected.findIndex(s => s.id === id);
    if (i > -1) { selected.splice(i, 1); b.classList.add('release'); setTimeout(() => b.classList.remove('release'), 450); }
    else { if (selected.length >= 8) return toast('Tối đa 8 ghế mỗi đơn'); selected.push({ id, price: PRICE[b.dataset.t], t: b.dataset.t }); }
    b.classList.toggle('selected', i === -1); b.setAttribute('aria-pressed', i === -1);
    if (i === -1) { b.classList.remove('pulse'); void b.offsetWidth; b.classList.add('pulse'); }
    onToggle();
  };
}


function initModal() {
  $('#close-modal').onclick = closeModal;
  $('#booking-modal').onmousedown = e => { if (e.target.id === 'booking-modal') closeModal(); };
  addEventListener('keydown', e => { if (e.key !== 'Escape') return; if (!$('#adm-dialog').hidden) closeDlg(); else if ($('#booking-modal').classList.contains('active')) closeModal(); else closeDetail(); });
  $('#btn-next-step').onclick = nextStep;
  $('#btn-prev-step').onclick = () => S.step > 1 && setStep(S.step - 1);
  $$('.step-item').forEach(el => el.onclick = () => { const n = +el.dataset.s; if (n < S.step && !$('#pay-success').innerHTML) setStep(n); });
  $('#promo-apply').onclick = () => applyPromo();
  $('#promo-input').onkeydown = e => { if (e.key === 'Enter') applyPromo(); };
  $('#promo-list').onclick = e => { const c = e.target.closest('.promo-chip'); if (c) applyPromo(c.dataset.code); };
  $('#combo-grid').onclick = e => { const b = e.target.closest('[data-d]'); if (b) changeCombo(b.dataset.id, +b.dataset.d); };
  renderPromoList(); renderCombos();
}
function openBooking(movie, o = {}) {
  const key0 = showKey(S.show);
  const next = { m: movie, date: o.date || S.stDate, cin: o.cin || CINEMAS.find(c => c.fm.some(f => movie.fm.includes(f))) || CINEMAS[0], time: o.time || '19:30' };
  if (showKey(next) !== key0 || !S.sel.length) { S.sel = []; stopHold(); }
  S.show = next; S.step = 1;
  $('#pay-form').hidden = false; $('#pay-processing').hidden = true; $('#pay-success').hidden = true; $('#pay-success').innerHTML = '';
  $('#sum-poster').src = img(movie.p, 200); $('#sum-title').textContent = movie.t;
  $('#sum-info').textContent = `${next.cin.n} · ${next.cin.room}`; $('#sum-time').textContent = `${next.time} · ${next.date}`;
  $('#screen-name').textContent = 'MÀN HÌNH ' + next.cin.room.replace('Phòng ', '').toUpperCase();
  drawSeats($('#seat-matrix'), soldFor(next), S.sel, update);
  $('#booking-modal').classList.add('active'); document.body.classList.add('modal-open');
  setStep(1, true); update();
}
function closeModal() { $('#booking-modal').classList.remove('active'); if ($('#detail').hidden) document.body.classList.remove('modal-open'); }

function setStep(n, first) {
  const dir = n >= S.step ? 'from-r' : 'from-l'; S.step = n;
  $$('.step-pane').forEach(p => p.classList.remove('active', 'from-r', 'from-l'));
  const pane = $('#pane-step-' + n); pane.classList.add('active'); if (!first) pane.classList.add(dir);
  $$('.step-item').forEach(el => { const s = +el.dataset.s; el.classList.toggle('active', s === n); el.classList.toggle('done', s < n); el.classList.toggle('locked', s > n); });
  $('#btn-prev-step').style.visibility = n === 1 ? 'hidden' : 'visible';
  $('#btn-next-step').textContent = n === 4 ? `Thanh toán ${fmt(S.ticker)}` : 'Tiếp tục';
  if (n === 2) renderCombos();
  if (n === 3) renderPromoList();
  if (n === 4) renderReview();
  pane.scrollTop = 0;
}
function nextStep() {
  if (S.step === 1 && !S.sel.length) return toast('Hãy chọn ít nhất một ghế để tiếp tục');
  S.step < 4 ? setStep(S.step + 1) : pay();
}

function update() {
  const t = totals(S.sel, S.combos, S.promo);
  $('#seat-cost').textContent = fmt(t.seat); $('#combo-cost').textContent = fmt(t.combo);
  $('#discount-cost').textContent = '-' + fmt(t.disc); $('#disc-label').textContent = S.promo ? `Ưu đãi ${S.promo}` : 'Ưu đãi';
  $('#selected-seats-list').innerHTML = S.sel.length ? S.sel.map(s => `<span class="seat-pill">${s.id}${s.t === 'couple' ? ' · Đôi' : s.t === 'vip' ? ' · VIP' : ''}</span>`).join('') : '<span class="empty-text">Chưa chọn ghế</span>';
  $('#seat-live').innerHTML = S.sel.length ? `<strong>${S.sel.length} ghế được chọn</strong> · ${fmt(t.seat)}` : 'Chưa chọn ghế';
  tick(S.ticker, t.total); S.ticker = t.total;
  if (S.sel.length) startHold(); else stopHold();
  if (S.step === 4) $('#btn-next-step').textContent = `Thanh toán ${fmt(t.total)}`;
  if (S.step === 2) renderCombos();
  if (S.step === 3) renderPromoList();
}
function tick(a, b) {
  const el = $('#total-price-ticker'); cancelAnimationFrame(S.raf);
  if (a === b || reduce) { el.textContent = fmt(b); return; }
  let t0; const f = ts => { t0 ??= ts; const p = Math.min((ts - t0) / 450, 1); el.textContent = fmt(Math.round(a + (b - a) * (1 - (1 - p) ** 3))); if (p < 1) S.raf = requestAnimationFrame(f); };
  S.raf = requestAnimationFrame(f);
}

/* Giữ ghế */
function startHold() {
  if (S.hold.t) return; S.hold.left = 600; $('#hold').hidden = false; paintHold();
  S.hold.t = setInterval(() => { S.hold.left--; paintHold(); if (S.hold.left <= 0) expireHold(); }, 1000);
}
function stopHold() { clearInterval(S.hold.t); S.hold.t = null; $('#hold').hidden = true; }
function paintHold() {
  const l = S.hold.left, h = $('#hold');
  $('#hold-time').textContent = `${String(l / 60 | 0).padStart(2, '0')}:${String(l % 60).padStart(2, '0')}`;
  $('#hold-fill').style.width = l / 6 + '%';
  h.classList.toggle('warn', l <= 120 && l > 30); h.classList.toggle('danger', l <= 30);
}
function expireHold() {
  stopHold(); S.sel = []; S.combos = { duo: 0, solo: 0 }; S.promo = null;
  drawSeats($('#seat-matrix'), soldFor(S.show), S.sel, update); setStep(1); update();
  toast('Hết thời gian giữ ghế. Vui lòng chọn lại');
}

/* Combo */
function renderCombos() {
  $('#combo-grid').innerHTML = COMBOS.map(c => { const q = S.combos[c.id] || 0;
    return `<div class="combo-card${q ? ' picked' : ''}"><div class="combo-img-wrap"><img src="${img(c.i)}" alt="${c.n}" loading="lazy" onerror="this.classList.add('img-fail')">${q ? '<span class="picked-tag">✓ Đã thêm</span>' : ''}</div>
    <div class="combo-details"><h4>${c.n}</h4><p>${c.d}</p><div class="combo-price-row"><span class="price">${fmt(c.p)}</span>
    <div class="qty-stepper"><button data-id="${c.id}" data-d="-1" aria-label="Giảm">−</button><span class="${q ? 'pop' : ''}">${q}</span><button data-id="${c.id}" data-d="1" aria-label="Tăng">+</button></div></div>
    ${q ? `<div class="combo-sub">${q} × ${fmt(c.p)} = <strong>${fmt(q * c.p)}</strong></div>` : ''}</div></div>`; }).join('');
}
function changeCombo(id, d) { S.combos[id] = Math.min(10, Math.max(0, (S.combos[id] || 0) + d)); update(); }

/* Ưu đãi */
function renderPromoList() {
  const t0 = totals(S.sel, S.combos, null);
  $('#promo-list').innerHTML = Object.entries(PROMOS).map(([k, p]) => { const on = S.promo === k;
    return `<button class="promo-chip${on ? ' on' : ''}" data-code="${k}"><strong>${k}${on ? ' · Đã áp dụng' : ''}</strong><span>${p.t}</span>${on ? `<em>-${fmt(Math.min(p.c(t0.seat, t0.combo), t0.seat + t0.combo))}</em>` : ''}</button>`; }).join('');
}
function applyPromo(code) {
  const inp = $('#promo-input'), msg = $('#promo-msg'); if (code) inp.value = code;
  const c = inp.value.trim().toUpperCase(); msg.className = 'promo-msg';
  if (!c) { msg.textContent = 'Vui lòng nhập mã ưu đãi.'; msg.classList.add('error'); return; }
  if (!PROMOS[c]) { S.promo = null; msg.textContent = `Mã "${c}" không hợp lệ hoặc đã hết hạn.`; msg.classList.add('error'); return update(); }
  if (S.promo === c && code) { S.promo = null; inp.value = ''; msg.textContent = `Đã bỏ mã ${c}.`; return update(); }
  const before = totals(S.sel, S.combos, null).total, after = totals(S.sel, S.combos, c);
  S.promo = c; msg.classList.add('ok');
  msg.textContent = `✓ Đã áp dụng ${c} · Giảm ${fmt(after.disc)}` + (before ? ` (${fmt(before)} → ${fmt(after.total)})` : '. Chọn ghế để thấy số tiền được giảm.');
  update();
}

/* Thanh toán */
function renderReview() {
  const t = totals(S.sel, S.combos, S.promo), cb = COMBOS.filter(c => S.combos[c.id]).map(c => `${S.combos[c.id]} × ${c.n}`).join(', ') || 'Không';
  $('#review').innerHTML = `<div><span>Phim</span><b>${S.show.m.t}</b></div><div><span>Suất chiếu</span><b>${S.show.time} · ${S.show.date} · ${S.show.cin.n}</b></div><div><span>Ghế</span><b>${S.sel.map(s => s.id).join(', ')}</b></div><div><span>Bắp nước</span><b>${cb}</b></div><div><span>Ưu đãi</span><b>${S.promo || 'Không'}</b></div>`;
}
function pay() {
  const method = $('input[name="pay"]:checked').value, t = totals(S.sel, S.combos, S.promo);
  $('#pay-form').hidden = true; $('#pay-processing').hidden = false;
  $('#btn-next-step').disabled = true; $('#btn-prev-step').disabled = true; stopHold();
  setTimeout(() => {
    const code = 'CNV-' + (8850 + Math.random() * 99 | 0), seats = S.sel.map(s => s.id);
    (S.booked[showKey(S.show)] ||= []).push(...seats);
    S.lastTicket = { code, movie: S.show.m.t, cin: S.show.cin.n, time: S.show.time, date: S.show.date, seats: seats.join(', '), total: t.total };
    addOrder(code, S.show.m.t, S.show.cin.n, S.show.time, seats.join(', '), t.total);
    $('#pay-processing').hidden = true;
    $('#pay-success').innerHTML = `<div class="success-check">✓</div><h3>Đặt vé thành công</h3><p class="muted">Đã thanh toán ${fmt(t.total)} qua ${method}</p>
      <div class="ticket"><div class="ticket-info"><h4>${S.show.m.t}</h4><dl><dt>Suất</dt><dd>${S.show.time} · ${S.show.date}</dd><dt>Rạp</dt><dd>${S.show.cin.n}</dd><dt>Phòng</dt><dd>${S.show.cin.room}</dd><dt>Ghế</dt><dd>${seats.join(', ')}</dd></dl><p class="ticket-code">Mã vé ${code}</p></div><div class="ticket-qr">${qr(code + seats.join(''))}</div></div>
      <div class="ticket-actions"><button class="btn-glass" onclick="downloadTicket()">Tải vé</button><button class="btn-primary-gold" id="btn-home">Về trang chủ</button></div>`;
    $('#pay-success').hidden = false; $('.summary-actions').style.display = 'none';
    $('#btn-home').onclick = finish;
  }, 1600);
}
function finish() {
  S.sel = []; S.combos = { duo: 0, solo: 0 }; S.promo = null; $('#promo-input').value = ''; $('#promo-msg').textContent = '';
  $('.summary-actions').style.display = ''; $('#btn-next-step').disabled = false; $('#btn-prev-step').disabled = false;
  closeModal(); closeDetail(); update(); scrollTo({ top: 0, behavior: 'smooth' }); toast('Vé đã được gửi vào email của bạn');
}
function qr(text) {
  let h = 2166136261; for (const c of text) h = Math.imul(h ^ c.charCodeAt(0), 16777619) >>> 0;
  const n = 21, cell = 5; let r = '';
  const fin = (x, y) => (x < 7 && y < 7) || (x > 13 && y < 7) || (x < 7 && y > 13);
  for (let y = 0; y < n; y++) for (let x = 0; x < n; x++) {
    let on;
    if (fin(x, y)) { const lx = x % 14, ly = y % 14; on = lx === 0 || ly === 0 || lx === 6 || ly === 6 || (lx > 1 && lx < 5 && ly > 1 && ly < 5); }
    else { h = (Math.imul(h, 1664525) + 1013904223) >>> 0; on = (h >> 16) & 1; }
    if (on) r += `<rect x="${x * cell}" y="${y * cell}" width="${cell}" height="${cell}"/>`;
  }
  return `<svg viewBox="0 0 ${n * cell} ${n * cell}" role="img" aria-label="Mã QR vé" fill="#000">${r}</svg>`;
}


const D = { m: null, cin: null, date: DATES[0].k };
const showTimes = c => TIMES.filter((t, i) => (i + c.id.charCodeAt(0)) % 5 !== 4);
const fitCinemas = m => { const l = CINEMAS.filter(c => c.fm.some(f => m.fm.includes(f))); return l.length ? l : CINEMAS; };
function initDetail() {
  $('#detail').onclick = e => {
    if (e.target.closest('[data-close]')) return closeDetail();
    const c = e.target.closest('[data-cin]'); if (c) { D.cin = CINEMAS.find(x => x.id === c.dataset.cin); return renderBook(); }
    const dt = e.target.closest('[data-date]'); if (dt) { D.date = dt.dataset.date; return renderBook(); }
    const t = e.target.closest('.time-chip'); if (t && !t.disabled) return openBooking(D.m, { cin: D.cin, time: t.dataset.t, date: D.date });
    if (e.target.closest('[data-trailer]')) toast('Trailer sẽ được cập nhật sớm');
    if (e.target.closest('[data-scroll]')) $('#d-book').scrollIntoView({ behavior: 'smooth' });
  };
}
function openDetail(m, toBook) {
  D.m = m; D.cin = fitCinemas(m)[0]; D.date = DATES[0].k;
  const d = $('#detail');
  d.innerHTML = `<div class="d-hero" style="background-image:url('${img(m.bd, 1600)}')"><div class="d-shade"></div>
    <button class="d-close" data-close aria-label="Đóng chi tiết phim">✕ Đóng</button>
    <div class="d-head"><div class="d-poster">${poster(m)}</div><div class="d-info">
      <span class="badge-gold">${m.st === 'now' ? 'ĐANG CHIẾU' : 'SẮP CHIẾU'}</span><h2>${m.t}</h2><p class="hero-subtitle">${m.sub || ''}</p>
      <div class="hero-metadata"><span class="meta-rating">★ ${m.r}</span><span>${m.d}</span><span class="badge-age">${m.age}</span><span>${m.g}</span></div>
      <p class="d-desc">${m.desc || ''}</p>
      <dl class="d-dl"><dt>Đạo diễn</dt><dd>${m.dir || '—'}</dd><dt>Diễn viên</dt><dd>${m.cast || '—'}</dd><dt>Định dạng</dt><dd>${m.fm.join(', ')}</dd></dl>
      <div class="hero-actions"><button class="btn-primary-gold" data-scroll>Đặt vé</button><button class="btn-glass" data-trailer>▶ Xem trailer</button></div></div></div></div>
    <section class="d-book" id="d-book"></section>`;
  renderBook(); d.hidden = false; d.scrollTop = 0; document.body.classList.add('modal-open');
  if (toBook) setTimeout(() => $('#d-book').scrollIntoView({ behavior: 'smooth' }), 80);
}
function renderBook() {
  const m = D.m, el = $('#d-book'); if (!el) return;
  if (m.st !== 'now') { el.innerHTML = `<h3>Đặt vé</h3><p class="muted">${m.t} chưa mở bán vé. Vui lòng quay lại sau.</p>`; return; }
  const n = new Date(), hhmm = `${String(n.getHours()).padStart(2, '0')}:${String(n.getMinutes()).padStart(2, '0')}`, today = D.date === DATES[0].k;
  el.innerHTML = `<h3>Đặt vé</h3>
  <div class="d-step"><span class="d-n">1</span>Chọn rạp</div><div class="filter-pills">${fitCinemas(m).map(c => `<button class="pill${c.id === D.cin.id ? ' active' : ''}" data-cin="${c.id}">${c.n}</button>`).join('')}</div>
  <div class="d-step"><span class="d-n">2</span>Chọn ngày</div><div class="filter-pills">${DATES.map(d => `<button class="pill${d.k === D.date ? ' active' : ''}" data-date="${d.k}">${d.l}</button>`).join('')}</div>
  <div class="d-step"><span class="d-n">3</span>Chọn suất chiếu · ${D.cin.room}</div><div class="st-times">${showTimes(D.cin).map(t => `<button class="time-chip" data-t="${t}" ${today && t < hhmm ? 'disabled' : ''}>${t}</button>`).join('')}</div>`;
}
function closeDetail() { $('#detail').hidden = true; if (!$('#booking-modal').classList.contains('active')) document.body.classList.remove('modal-open'); }

function renderHeroNext(m) {
  const cin = fitCinemas(m)[0], n = new Date(), now = `${String(n.getHours()).padStart(2, '0')}:${String(n.getMinutes()).padStart(2, '0')}`;
  let list = showTimes(cin).filter(t => t >= now).map(t => ({ t, d: DATES[0] }));
  if (list.length < 3) list = list.concat(showTimes(cin).map(t => ({ t, d: DATES[1] })));
  list = list.slice(0, 3);
  $('#hero-next').innerHTML = `<span class="hero-next-label">Suất gần nhất · ${cin.n}</span>` + list.map(x => `<button class="time-chip" data-t="${x.t}" data-d="${x.d.k}">${x.t}${x.d === DATES[1] ? ' ngày mai' : ''}</button>`).join('');
  $('#hero-next').onclick = e => { const b = e.target.closest('.time-chip'); if (b) openBooking(heroList[heroIdx], { cin, time: b.dataset.t, date: b.dataset.d }); };
}

/* ================= VÉ, ĐƠN HÀNG ================= */
function addOrder(code, movie, cin, time, seats, total, st = 'ok') { DB.orders.unshift({ id: code, code: '#' + code, movie, cin, time, seats, total, st }); }
function downloadTicket(t = S.lastTicket) {
  if (!t) return;
  const txt = ['CINEVERSE - VÉ ĐIỆN TỬ', `Mã vé: ${t.code}`, `Phim: ${t.movie}`, `Rạp: ${t.cin}`, `Suất: ${t.time} · ${t.date}`, `Ghế: ${t.seats}`, `Tổng tiền: ${fmt(t.total)}`].join('\n');
  const a = document.createElement('a'); a.href = URL.createObjectURL(new Blob([txt], { type: 'text/plain;charset=utf-8' })); a.download = `${t.code}.txt`; a.click();
  setTimeout(() => URL.revokeObjectURL(a.href), 1000); toast('Đã tải vé');
}


const MEMBERS = {
  '0908123456': { n: 'Lê Minh Khoa', tier: 'Thành viên Vàng', promo: 'CINE2026' },
  '0912345678': { n: 'Phạm Thu Hà', tier: 'Học sinh, sinh viên', promo: 'HSSV' },
  '0933000111': { n: 'Ngô Quang Huy', tier: 'Thành viên Bạc', promo: '' }
};
let posShows = [];
function initPOS() {
  const P = S.pos;
  posShows = MOVIES.filter(m => m.st === 'now').slice(0, 4).map((m, i) => ({ m, date: DATES[0].k, cin: CINEMAS[0], time: ['19:30', '20:15', '21:45', '16:15'][i] }));
  P.show = posShows[0];
  $('#pos-shows').innerHTML = posShows.map((s, i) => `<button class="pos-movie-btn${i ? '' : ' active'}" data-i="${i}"><strong>${s.m.t}</strong><span>${s.time} · ${s.cin.room}</span></button>`).join('');
  $('#pos-shows').onclick = e => { const b = e.target.closest('.pos-movie-btn'); if (!b) return; $$('#pos-shows .pos-movie-btn').forEach(x => x.classList.toggle('active', x === b)); P.show = posShows[+b.dataset.i]; P.sel = []; P.paid = false; drawPos(); };
  fillPosPromos();
  $('#pos-promo').onchange = e => { P.promo = e.target.value; P.auto = false; posTotals(); };
  $('#pos-phone').oninput = e => {
    const v = e.target.value.replace(/\D/g, ''); e.target.value = v; const m = MEMBERS[v], box = $('#pos-member');
    if (m) { box.textContent = `${m.n} · ${m.tier}${m.promo ? ' · tự áp ' + m.promo : ''}`; if (m.promo && PROMOS[m.promo]) { P.promo = m.promo; P.auto = true; $('#pos-promo').value = m.promo; } }
    else { box.textContent = v.length === 10 ? 'Khách vãng lai, chưa có thẻ' : ''; if (P.auto) { P.promo = ''; P.auto = false; $('#pos-promo').value = ''; } }
    posTotals();
  };
  renderPosCombos();
  $$('.btn-pos-pay').forEach(b => b.onclick = () => posPay(b.dataset.m));
  $('#pos-cancel').onclick = posCancel;
  drawPos();
}
function fillPosPromos() { const s = $('#pos-promo'), cur = S.pos.promo; s.innerHTML = '<option value="">Không áp dụng</option>' + Object.entries(PROMOS).map(([k, p]) => `<option value="${k}">${k} · ${p.t}</option>`).join(''); s.value = PROMOS[cur] ? cur : ''; }
function renderPosCombos() {
  const P = S.pos; $('#pos-combos').innerHTML = COMBOS.map(c => `<div class="pos-combo"><span>${c.n}<small>${fmt(c.p)}</small></span><div class="qty-stepper"><button data-id="${c.id}" data-d="-1">−</button><span>${P.combos[c.id] || 0}</span><button data-id="${c.id}" data-d="1">+</button></div></div>`).join('');
  $('#pos-combos').onclick = e => { const b = e.target.closest('[data-d]'); if (!b) return; P.combos[b.dataset.id] = Math.min(10, Math.max(0, (P.combos[b.dataset.id] || 0) + +b.dataset.d)); renderPosCombos(); posTotals(); };
}
function posState(t, k) { const el = $('#pos-state'); el.textContent = t; el.className = 'pos-state ' + (k || ''); }
function drawPos() {
  const P = S.pos; $('#pos-room').textContent = `· ${P.show.cin.room}`; $('#pos-status').textContent = '';
  drawSeats($('#pos-seats'), soldFor(P.show), P.sel, posTotals); posTotals();
}
function posTotals() {
  const P = S.pos, t = totals(P.sel, P.combos, P.promo);
  const cb = COMBOS.filter(c => P.combos[c.id]).map(c => `<div><span>${c.n} × ${P.combos[c.id]}</span><b>${fmt(c.p * P.combos[c.id])}</b></div>`).join('');
  $('#pos-lines').innerHTML = `<div class="pos-line-head"><b>${P.show.m.t}</b><span>${P.show.time} · ${P.show.cin.room}</span></div>` +
    (P.sel.length ? P.sel.map(s => `<div><span>Ghế ${s.id} · ${TYPE_NAME[s.t]}</span><b>${fmt(s.price)}</b></div>`).join('') : '<p class="muted">Chưa chọn ghế</p>') + cb + (t.disc ? `<div class="hl"><span>Khuyến mãi ${P.promo}</span><b>-${fmt(t.disc)}</b></div>` : '');
  $('#pos-total').textContent = fmt(t.total);
  const tk = $('#pos-ticket');
  if (P.sel.length) { P.paid = false; tk.className = 'pos-ticket preview'; tk.hidden = false; tk.innerHTML = `<div><em>Xem trước vé</em><strong>${P.show.m.t}</strong><span>${P.show.time} · ${P.show.cin.room}</span><span>Ghế ${P.sel.map(s => s.id).join(', ')}</span><span>${fmt(t.total)}</span></div>`; posState('Đang soạn đơn', 'wait'); }
  else if (!P.paid) { tk.hidden = true; posState('Sẵn sàng', ''); }
}
function posPay(method) {
  const P = S.pos, st = $('#pos-status'), phone = $('#pos-phone').value; st.className = 'pos-status';
  if (!P.sel.length) { st.textContent = 'Chọn ít nhất một ghế trước khi thanh toán.'; st.classList.add('error'); return; }
  if (phone && !/^0\d{9}$/.test(phone)) { st.textContent = 'Số điện thoại phải gồm 10 chữ số, bắt đầu bằng 0.'; st.classList.add('error'); return; }
  const t = totals(P.sel, P.combos, P.promo), code = 'CNV-' + (8850 + Math.random() * 99 | 0), seats = P.sel.map(s => s.id), key = showKey(P.show), show = P.show;
  (S.booked[key] ||= []).push(...seats); addOrder(code, show.m.t, show.cin.n, show.time, seats.join(', '), t.total);
  P.last = { code, key, seats, movie: show.m.t, cin: show.cin.n, time: show.time, date: show.date, total: t.total };
  P.sel = []; P.combos = {}; P.promo = ''; P.auto = false; $('#pos-promo').value = ''; $('#pos-phone').value = ''; $('#pos-member').textContent = '';
  renderPosCombos(); drawSeats($('#pos-seats'), soldFor(show), P.sel, posTotals); posTotals();
  const tk = $('#pos-ticket'); P.paid = true; tk.hidden = false; tk.className = 'pos-ticket';
  tk.innerHTML = `<div><strong>${show.m.t}</strong><span>${show.time} · ${show.cin.room}</span><span>Ghế ${seats.join(', ')} · ${code}</span><span class="pos-btns"><button class="btn-glass" onclick="print()">In vé</button><button class="btn-glass" onclick="downloadTicket(S.pos.last)">Xuất vé</button><button class="btn-glass" onclick="posRefund()">Hoàn vé</button></span></div>${qr(code + seats.join(''))}`;
  posState('Đã thanh toán', 'ok'); st.textContent = `Đã thu ${fmt(t.total)} bằng ${method}.`; st.classList.add('ok');
}
function posRefund() {
  const L = S.pos.last; if (!L) return;
  const set = S.booked[L.key] || []; S.booked[L.key] = set.filter(id => !L.seats.includes(id));
  const o = DB.orders.find(x => x.id === L.code); if (o) o.st = 'cancel';
  S.pos.paid = false; S.pos.last = null; $('#pos-ticket').hidden = true; drawSeats($('#pos-seats'), soldFor(S.pos.show), S.pos.sel, posTotals);
  posState('Đã hoàn vé', 'bad'); $('#pos-status').className = 'pos-status ok'; $('#pos-status').textContent = `Đã hoàn vé ${L.code}, ghế ${L.seats.join(', ')} được trả lại.`;
}
function posCancel() {
  const P = S.pos; if (!P.sel.length && !Object.values(P.combos).some(Boolean)) return toast('Chưa có giao dịch để hủy');
  P.sel = []; P.combos = {}; P.promo = ''; P.auto = false; $('#pos-promo').value = ''; renderPosCombos(); drawSeats($('#pos-seats'), soldFor(P.show), P.sel, posTotals); posTotals();
  posState('Đã hủy giao dịch', 'bad'); $('#pos-status').textContent = ''; toast('Đã hủy giao dịch, ghế được trả lại');
}


const DB = {
  movies: MOVIES, cinemas: CINEMAS, combos: COMBOS,
  promos: [
    { id: 'p1', code: 'CINE2026', desc: 'Giảm 10% toàn bộ đơn', type: 'percent', value: 10, st: 'on' },
    { id: 'p2', code: 'HSSV', desc: 'Giảm 20.000đ cho học sinh, sinh viên', type: 'fixed', value: 20000, st: 'on' },
    { id: 'p3', code: 'COMBO15', desc: 'Giảm 15% tiền bắp nước', type: 'combo', value: 15, st: 'on' }
  ],
  customers: [
    { id: 'c1', n: 'Lê Minh Khoa', phone: '0908123456', tier: 'gold', st: 'on' },
    { id: 'c2', n: 'Phạm Thu Hà', phone: '0912345678', tier: 'student', st: 'on' },
    { id: 'c3', n: 'Ngô Quang Huy', phone: '0933000111', tier: 'silver', st: 'on' }
  ],
  staff: [
    { id: 's1', n: 'Nguyễn Văn A', role: 'Thu ngân', cin: 'Vincom Center Q1', shift: 'Chiều', st: 'on' },
    { id: 's2', n: 'Trần Thị B', role: 'Thu ngân', cin: 'Landmark 81', shift: 'Sáng', st: 'on' },
    { id: 's3', n: 'Lê Văn C', role: 'Quản lý ca', cin: 'Aeon Tân Phú', shift: 'Tối', st: 'on' }
  ],
  orders: [
    { id: 'CNV-8849', code: '#CNV-8849', movie: 'Demon Slayer', cin: 'Vincom Center Q1', time: '19:30', seats: 'E6, E7', total: 300000, st: 'ok' },
    { id: 'CNV-8848', code: '#CNV-8848', movie: 'Dune: Hành Tinh Cát II', cin: 'Landmark 81', time: '20:15', seats: 'F3', total: 280000, st: 'ok' },
    { id: 'CNV-8847', code: '#CNV-8847', movie: 'Kung Fu Panda 4', cin: 'Aeon Tân Phú', time: '18:00', seats: 'D3, D4, D5', total: 360000, st: 'wait' }
  ]
};
function syncPromos() {
  Object.keys(PROMOS).forEach(k => delete PROMOS[k]);
  DB.promos.filter(p => p.st === 'on').forEach(p => {
    const v = Number(p.value) || 0;
    PROMOS[p.code] = { t: p.desc, c: p.type === 'percent' ? (s, c) => Math.round((s + c) * v / 100) : p.type === 'combo' ? (s, c) => Math.round(c * v / 100) : (s, c) => Math.min(v, s + c) };
  });
  if (S.promo && !PROMOS[S.promo]) S.promo = null;
  if (S.pos.promo && !PROMOS[S.pos.promo]) S.pos.promo = '';
}
function syncCombos() {
  COMBOS.forEach(c => { c.p = Number(c.p) || 0; S.combos[c.id] ??= 0; S.pos.combos[c.id] ??= 0; });
  renderPosCombos(); posTotals(); update();
}
const ST3 = (a, b, c) => [['on', a], ['off', b]];
const MOD = {
  movies: { title: 'Phim và suất chiếu', f: [{ k: 't', l: 'Tên phim', req: 1 }, { k: 'dir', l: 'Đạo diễn' }, { k: 'cast', l: 'Diễn viên' }, { k: 'g', l: 'Thể loại' }, { k: 'fm', l: 'Định dạng (cách nhau dấu phẩy)', list: 1 }, { k: 'd', l: 'Thời lượng' }, { k: 'st', l: 'Trạng thái', sel: [['now', 'Đang chiếu'], ['soon', 'Sắp chiếu'], ['off', 'Ngừng chiếu']] }], cols: ['t', 'dir', 'fm', 'd', 'st'], def: () => ({ id: 'm' + Date.now(), sub: '', p: 'photo-1489599849927-2ee91cede3ba', bd: 'photo-1489599849927-2ee91cede3ba', age: 'T13', r: 0, desc: '', fm: ['2D'], st: 'soon' }) },
  cinemas: { title: 'Rạp và phòng chiếu', f: [{ k: 'n', l: 'Tên rạp', req: 1 }, { k: 'a', l: 'Địa chỉ' }, { k: 'rooms', l: 'Số phòng', num: 1 }, { k: 'fm', l: 'Định dạng (cách nhau dấu phẩy)', list: 1 }, { k: 'room', l: 'Tên phòng chính' }], cols: ['n', 'a', 'rooms', 'fm'], def: () => ({ id: 'r' + Date.now().toString(36), rooms: 1, fm: ['2D'], room: 'Phòng 2D' }) },
  combos: { title: 'Combo bắp nước', f: [{ k: 'n', l: 'Tên combo', req: 1 }, { k: 'd', l: 'Thành phần' }, { k: 'p', l: 'Giá (đồng)', num: 1, money: 1 }], cols: ['n', 'd', 'p'], def: () => ({ id: 'cb' + Date.now().toString(36), i: 'photo-1578849278619-e73505e9610f', p: 0 }) },
  promos: { title: 'Khuyến mãi', f: [{ k: 'code', l: 'Mã', req: 1, upper: 1 }, { k: 'desc', l: 'Mô tả' }, { k: 'type', l: 'Loại giảm', sel: [['percent', 'Phần trăm toàn đơn'], ['fixed', 'Số tiền cố định'], ['combo', 'Phần trăm tiền bắp nước']] }, { k: 'value', l: 'Giá trị', num: 1 }, { k: 'st', l: 'Trạng thái', sel: [['on', 'Đang chạy'], ['off', 'Tạm dừng']] }], cols: ['code', 'desc', 'type', 'value', 'st'], def: () => ({ id: 'p' + Date.now(), type: 'percent', value: 10, st: 'on' }) },
  customers: { title: 'Khách hàng', f: [{ k: 'n', l: 'Họ tên', req: 1 }, { k: 'phone', l: 'Số điện thoại', phone: 1 }, { k: 'tier', l: 'Loại khách', sel: [['regular', 'Thường'], ['silver', 'Thành viên Bạc'], ['gold', 'Thành viên Vàng'], ['student', 'Học sinh, sinh viên']] }, { k: 'st', l: 'Trạng thái', sel: [['on', 'Hoạt động'], ['off', 'Đã khóa']] }], cols: ['n', 'phone', 'tier', 'st'], def: () => ({ id: 'c' + Date.now(), tier: 'regular', st: 'on' }) },
  orders: { title: 'Đơn hàng và vé', f: [{ k: 'code', l: 'Mã đơn', req: 1 }, { k: 'movie', l: 'Phim' }, { k: 'cin', l: 'Rạp' }, { k: 'time', l: 'Suất chiếu' }, { k: 'seats', l: 'Ghế' }, { k: 'total', l: 'Tổng tiền (đồng)', num: 1, money: 1 }, { k: 'st', l: 'Trạng thái', sel: [['ok', 'Thành công'], ['wait', 'Đang xử lý'], ['cancel', 'Đã hủy']] }], cols: ['code', 'movie', 'cin', 'time', 'seats', 'total', 'st'], def: () => ({ id: 'o' + Date.now(), st: 'wait' }) },
  staff: { title: 'Nhân sự', f: [{ k: 'n', l: 'Họ tên', req: 1 }, { k: 'role', l: 'Vai trò', sel: [['Thu ngân', 'Thu ngân'], ['Quản lý ca', 'Quản lý ca'], ['Soát vé', 'Soát vé']] }, { k: 'cin', l: 'Rạp' }, { k: 'shift', l: 'Ca làm', sel: [['Sáng', 'Sáng'], ['Chiều', 'Chiều'], ['Tối', 'Tối']] }, { k: 'st', l: 'Trạng thái', sel: [['on', 'Đang làm'], ['off', 'Nghỉ việc']] }], cols: ['n', 'role', 'cin', 'shift', 'st'], def: () => ({ id: 's' + Date.now(), role: 'Thu ngân', shift: 'Sáng', st: 'on' }) }
};
const badge = (t, k) => `<span class="status-badge ${k}">${t}</span>`;
const table = (cols, rows) => `<table class="adm-table"><thead><tr>${cols.map(c => `<th>${c}</th>`).join('')}</tr></thead><tbody>${rows.map(r => `<tr>${r.map(c => `<td>${c}</td>`).join('')}</tr>`).join('')}</tbody></table>`;
const esc = s => String(s ?? '').replace(/[&<>"]/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;' }[c]));
const A = { v: 'dash', range: 7, q: '', f: 'all' };
const rng = seed => () => (seed = (seed * 16807) % 2147483647) / 2147483647;

function cell(mod, it, f) {
  const v = it[f.k];
  if (f.sel) { const i = f.sel.findIndex(o => o[0] === v), lab = f.sel[i]?.[1] ?? v; return f.k === 'st' ? `<button class="status-badge ${['success', 'pending', 'muted'][Math.min(i < 0 ? 2 : i, 2)]} st-btn" data-act="st" data-id="${it.id}" title="Bấm để đổi trạng thái">${lab}</button>` : lab; }
  if (f.list) return esc((v || []).join(', '));
  if (f.money) return fmt(Number(v) || 0);
  if (mod === 'promos' && f.k === 'value') return it.type === 'fixed' ? fmt(Number(v)) : v + '%';
  return f.k === 'code' || f.k === 'n' || f.k === 't' ? `<strong>${esc(v)}</strong>` : esc(v);
}
function rowsOf(mod) {
  const M = MOD[mod], q = norm(A.q.trim());
  return DB[mod].filter(it => (!q || norm(M.f.map(f => Array.isArray(it[f.k]) ? it[f.k].join(' ') : it[f.k]).join(' ')).includes(q)) && (A.f === 'all' || it.st === A.f));
}
function renderRows() {
  const mod = A.v, M = MOD[mod], list = rowsOf(mod), fs = M.cols.map(k => M.f.find(f => f.k === k));
  $('#adm-rows').innerHTML = list.length ? list.map(it => `<tr>${fs.map(f => `<td>${cell(mod, it, f)}</td>`).join('')}<td class="row-acts"><button data-act="view" data-id="${it.id}">Xem</button><button data-act="edit" data-id="${it.id}">Sửa</button><button class="danger" data-act="del" data-id="${it.id}">Xóa</button></td></tr>`).join('') : `<tr><td colspan="${fs.length + 1}" class="adm-empty">Không có dữ liệu phù hợp. Thử xóa bộ lọc hoặc bấm Thêm mới.</td></tr>`;
  $('#adm-count').textContent = `${list.length} / ${DB[mod].length} mục`;
}
function renderModule() {
  const mod = A.v, M = MOD[mod], stf = M.f.find(f => f.k === 'st'), fs = M.cols.map(k => M.f.find(f => f.k === k));
  $('#adm-title').textContent = M.title;
  $('#adm-tools').innerHTML = `<button class="btn-adm-action" data-act="add">+ Thêm mới</button>`;
  $('#adm-content').innerHTML = `<div class="adm-table-card"><div class="adm-bar"><input type="search" id="adm-q" placeholder="Tìm trong ${M.title.toLowerCase()}..." value="${esc(A.q)}" aria-label="Tìm kiếm">${stf ? `<select id="adm-f" aria-label="Lọc trạng thái"><option value="all">Mọi trạng thái</option>${stf.sel.map(o => `<option value="${o[0]}"${A.f === o[0] ? ' selected' : ''}>${o[1]}</option>`).join('')}</select>` : ''}<span class="adm-count" id="adm-count"></span></div>
    <div class="adm-scroll"><table class="adm-table"><thead><tr>${fs.map(f => `<th>${f.l.replace(/ \(.*\)/, '')}</th>`).join('')}<th></th></tr></thead><tbody id="adm-rows"></tbody></table></div></div>`;
  $('#adm-q').oninput = e => { A.q = e.target.value; renderRows(); };
  if (stf) $('#adm-f').onchange = e => { A.f = e.target.value; renderRows(); };
  renderRows();
}
function dlg(html) { const d = $('#adm-dialog'); d.innerHTML = `<div class="adm-dlg-box" role="dialog" aria-modal="true">${html}</div>`; d.hidden = false; d.querySelector('input,select,button')?.focus(); }
function closeDlg() { $('#adm-dialog').hidden = true; }
function openForm(id) {
  const mod = A.v, M = MOD[mod], it = id ? DB[mod].find(x => x.id === id) : null;
  dlg(`<h3>${it ? 'Sửa' : 'Thêm'} · ${M.title}</h3><form id="adm-form" novalidate>${M.f.map(f => { const v = it ? it[f.k] : M.def()[f.k]; const val = Array.isArray(v) ? v.join(', ') : (v ?? '');
    return `<label>${f.l}${f.req ? ' *' : ''}${f.sel ? `<select name="${f.k}">${f.sel.map(o => `<option value="${o[0]}"${o[0] === val ? ' selected' : ''}>${o[1]}</option>`).join('')}</select>` : `<input name="${f.k}" type="${f.num ? 'number' : 'text'}" ${f.num ? 'min="0"' : ''} value="${esc(val)}">`}</label>`; }).join('')}
    <p class="form-err" id="form-err" role="alert"></p><div class="dlg-actions"><button type="button" class="btn-back-adm" data-x>Hủy</button><button type="submit" class="btn-adm-action">Lưu</button></div></form>`);
  $('#adm-form').onsubmit = e => { e.preventDefault(); saveForm(it); };
}
function saveForm(it) {
  const mod = A.v, M = MOD[mod], fd = new FormData($('#adm-form')), data = {}, err = $('#form-err');
  for (const f of M.f) {
    let v = String(fd.get(f.k) ?? '').trim();
    if (f.req && !v) return err.textContent = `Vui lòng nhập: ${f.l}.`;
    if (f.num) { v = Number(v); if (!(v >= 0)) return err.textContent = `${f.l} phải là số không âm.`; }
    if (f.phone && v && !/^0\d{9}$/.test(v)) return err.textContent = 'Số điện thoại gồm 10 chữ số, bắt đầu bằng 0.';
    if (f.upper) { v = v.toUpperCase(); if (DB[mod].some(x => x.code === v && x !== it)) return err.textContent = `Mã ${v} đã tồn tại.`; }
    data[f.k] = f.list ? (v.split(',').map(s => s.trim()).filter(Boolean).length ? v.split(',').map(s => s.trim()).filter(Boolean) : ['2D']) : v;
  }
  if (it) Object.assign(it, data); else DB[mod].unshift(Object.assign(M.def(), data));
  if (mod === 'orders' && !it) DB.orders[0].id = data.code;
  afterSave(mod); closeDlg(); renderRows(); toast(it ? 'Đã lưu thay đổi' : 'Đã thêm mới');
}
function afterSave(mod) {
  if (mod === 'movies') { initMovies(); renderShowtimes(); }
  if (mod === 'cinemas') { initShowtimes(); initStatic(); }
  if (mod === 'combos') syncCombos();
  if (mod === 'promos') { syncPromos(); initStatic(); fillPosPromos(); update(); }
}
function viewItem(it) {
  const M = MOD[A.v];
  dlg(`<h3>Chi tiết · ${M.title}</h3><dl class="adm-dl">${M.f.map(f => `<dt>${f.l.replace(/ \(.*\)/, '')}</dt><dd>${f.sel ? (f.sel.find(o => o[0] === it[f.k])?.[1] ?? '') : cell(A.v, it, f).replace(/<[^>]+>/g, '')}</dd>`).join('')}</dl><div class="dlg-actions"><button class="btn-back-adm" data-x>Đóng</button><button class="btn-adm-action" data-act="edit" data-id="${it.id}">Sửa</button></div>`);
}
function confirmDel(it) {
  const M = MOD[A.v], name = it.t || it.n || it.code || it.id;
  dlg(`<h3>Xóa "${esc(name)}"?</h3><p class="muted dark">Thao tác này không thể hoàn tác.</p><div class="dlg-actions"><button class="btn-back-adm" data-x>Giữ lại</button><button class="btn-adm-action danger-fill" id="del-ok">Xóa</button></div>`);
  $('#del-ok').onclick = () => { const i = DB[A.v].indexOf(it); if (i > -1) DB[A.v].splice(i, 1); afterSave(A.v); closeDlg(); renderRows(); toast('Đã xóa'); };
}
function initAdmin() {
  $('#adm-menu').onclick = e => { const a = e.target.closest('.adm-item'); if (!a) return; e.preventDefault(); A.v = a.dataset.v; A.q = ''; A.f = 'all'; $$('.adm-item').forEach(x => x.classList.toggle('active', x === a)); renderAdmin(); };
  $('#adm-tools').onclick = e => { const p = e.target.closest('.pill'); if (p) { A.range = +p.dataset.r; renderAdmin(); } };
  $('#view-admin').addEventListener('click', e => {
    const b = e.target.closest('[data-act]'); if (!b) return;
    const it = DB[A.v]?.find(x => x.id === b.dataset.id), act = b.dataset.act;
    if (act === 'add') openForm(); else if (act === 'edit') openForm(b.dataset.id);
    else if (act === 'view' && it) viewItem(it); else if (act === 'del' && it) confirmDel(it);
    else if (act === 'st' && it) { const f = MOD[A.v].f.find(x => x.k === 'st'), i = f.sel.findIndex(o => o[0] === it.st); it.st = f.sel[(i + 1) % f.sel.length][0]; afterSave(A.v); renderRows(); toast('Đã đổi trạng thái'); }
  });
  $('#adm-dialog').onclick = e => { if (e.target.id === 'adm-dialog' || e.target.closest('[data-x]')) closeDlg(); };
  syncPromos(); renderAdmin();
}
function renderAdmin() {
  const box = $('#adm-content'), tools = $('#adm-tools');
  box.classList.remove('enter'); void box.offsetWidth; box.classList.add('enter');
  if (A.v !== 'dash') return renderModule();
  $('#adm-title').textContent = 'Tổng quan hoạt động';
  tools.innerHTML = [[1, 'Hôm nay'], [7, '7 ngày'], [30, 'Tháng'], [90, 'Quý']].map(([r, l]) => `<button class="pill adm-pill${A.range === r ? ' active' : ''}" data-r="${r}">${l}</button>`).join('');
  const n = A.range === 1 ? 8 : A.range === 7 ? 7 : A.range === 30 ? 10 : 12, rnd = rng(A.range * 977), vals = Array.from({ length: n }, () => 20 + rnd() * 80 | 0);
  const sum = vals.reduce((a, b) => a + b, 0) * (A.range === 1 ? 1.2 : 1), max = Math.max(...vals), occ = 62 + (rnd() * 20 | 0), w = 560, h = 200, bw = w / n;
  const bars = vals.map((v, i) => { const bh = v / max * (h - 30); return `<g><rect x="${i * bw + 8}" y="${h - bh - 20}" width="${bw - 16}" height="${bh}" rx="5"><title>${v} triệu đồng</title></rect><text x="${i * bw + bw / 2}" y="${h - 4}" text-anchor="middle">${A.range === 1 ? 8 + i * 2 + 'h' : i + 1}</text></g>`; }).join('');
  const byMovie = MOVIES.filter(m => m.st !== 'off').slice(0, 5).map((m, i) => ({ t: m.t, v: 30 - i * 5 + (rnd() * 6 | 0) })), tot = byMovie.reduce((a, b) => a + b.v, 0) || 1;
  const stLab = { ok: ['Thành công', 'success'], wait: ['Đang xử lý', 'pending'], cancel: ['Đã hủy', 'muted'] };
  box.innerHTML = `<section class="adm-kpi-grid">
    <div class="kpi-card"><span class="kpi-title">DOANH THU</span><div class="kpi-value">${(sum * 1e6).toLocaleString('vi-VN')}đ</div><span class="kpi-change positive">↑ 14,2% so với kỳ trước</span></div>
    <div class="kpi-card"><span class="kpi-title">VÉ ĐÃ BÁN</span><div class="kpi-value">${Math.round(sum * 6.1).toLocaleString('vi-VN')}</div><span class="kpi-change positive">↑ 8,1%</span></div>
    <div class="kpi-card"><span class="kpi-title">ĐƠN HÀNG</span><div class="kpi-value">${(Math.round(sum * 2.4) + DB.orders.length).toLocaleString('vi-VN')}</div><span class="kpi-change">99,4% giao dịch thành công</span></div>
    <div class="kpi-card"><span class="kpi-title">TỈ LỆ LẤP ĐẦY</span><div class="kpi-value">${occ}%</div><span class="kpi-change positive">↑ 3,5 điểm</span></div></section>
  <section class="adm-two"><div class="adm-table-card"><h3>Doanh thu (triệu đồng)</h3><svg class="adm-chart" viewBox="0 0 ${w} ${h}" role="img" aria-label="Biểu đồ doanh thu">${bars}</svg></div>
    <div class="adm-table-card"><h3>Doanh thu theo phim</h3>${byMovie.map(b => `<div class="bar-row"><span>${b.t}</span><div class="bar-track"><i style="width:${b.v / tot * 220}%"></i></div><b>${Math.round(b.v / tot * 100)}%</b></div>`).join('')}</div></section>
  <div class="adm-table-card"><h3>Giao dịch gần đây</h3><div class="adm-scroll">${table(['Mã đơn', 'Phim', 'Rạp', 'Suất', 'Ghế', 'Tổng tiền', 'Trạng thái'], DB.orders.slice(0, 5).map(o => [`<code>${esc(o.code)}</code>`, `<strong>${esc(o.movie)}</strong>`, esc(o.cin), esc(o.time), esc(o.seats), fmt(Number(o.total) || 0), badge(...stLab[o.st] || stLab.wait)]))}</div></div>`;
}
