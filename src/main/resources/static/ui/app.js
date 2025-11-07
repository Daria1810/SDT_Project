/* Simple helper to POST/GET JSON to our REST API and show results */
const $ = (sel) => document.querySelector(sel);
const show = (id, data) => {
  const el = document.getElementById(id);
  el.textContent = (typeof data === 'string') ? data : JSON.stringify(data, null, 2);
};

async function postJson(url, body) {
  const res = await fetch(url, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) });
  if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
  return res.json();
}

async function getJson(url) {
  const res = await fetch(url);
  if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
  return res.json();
}

// Utility guarded binder to avoid errors if an element doesn't exist
function bind(selector, event, handler) {
  const el = document.querySelector(selector);
  if (el) el.addEventListener(event, handler);
}

// Users
bind('#form-register', 'submit', async (e) => {
  e.preventDefault();
  const fd = new FormData(e.target);
  const payload = {
    email: fd.get('email'),
    password: fd.get('password'),
    tier: fd.get('tier')
  };
  try { const data = await postJson('/api/users/register', payload); show('res-register', data); }
  catch (err) { show('res-register', `Error: ${err}`); }
});

bind('#form-get-user', 'submit', async (e) => {
  e.preventDefault();
  const id = Number(new FormData(e.target).get('id'));
  try { const data = await getJson(`/api/users/${id}`); show('res-get-user', data); }
  catch (err) { show('res-get-user', `Error: ${err}`); }
});

// Content - Movie
bind('#form-movie', 'submit', async (e) => {
  e.preventDefault();
  const fd = new FormData(e.target);
  const payload = {
    type: 'movie',
    title: fd.get('title'),
    description: fd.get('description'),
    genre: fd.get('genre'),
    releaseYear: Number(fd.get('releaseYear')),
    durationMinutes: Number(fd.get('durationMinutes'))
  };
  try { const data = await postJson('/api/content/movie', payload); show('res-movie', data); }
  catch (err) { show('res-movie', `Error: ${err}`); }
});

// Content - TV Series
bind('#form-tv', 'submit', async (e) => {
  e.preventDefault();
  const fd = new FormData(e.target);
  const payload = {
    type: 'tvseries',
    title: fd.get('title'),
    description: fd.get('description'),
    genre: fd.get('genre'),
    releaseYear: Number(fd.get('releaseYear')),
    seasons: Number(fd.get('seasons')),
    totalEpisodes: Number(fd.get('totalEpisodes')),
    averageEpisodeDuration: Number(fd.get('averageEpisodeDuration'))
  };
  try { const data = await postJson('/api/content/tvseries', payload); show('res-tv', data); }
  catch (err) { show('res-tv', `Error: ${err}`); }
});

bind('#form-get-content', 'submit', async (e) => {
  e.preventDefault();
  const id = Number(new FormData(e.target).get('id'));
  try { const data = await getJson(`/api/content/${id}`); show('res-get-content', data); }
  catch (err) { show('res-get-content', `Error: ${err}`); }
});

// Video
bind('#form-watch', 'submit', async (e) => {
  e.preventDefault();
  const fd = new FormData(e.target);
  const payload = {
    userId: Number(fd.get('userId')),
    contentId: Number(fd.get('contentId')),
    progressSeconds: Number(fd.get('progressSeconds')),
    completed: fd.get('completed') === 'true'
  };
  try { const data = await postJson('/api/video/watch', payload); show('res-watch', data); }
  catch (err) { show('res-watch', `Error: ${err}`); }
});

bind('#form-rate', 'submit', async (e) => {
  e.preventDefault();
  const fd = new FormData(e.target);
  const payload = {
    userId: Number(fd.get('userId')),
    contentId: Number(fd.get('contentId')),
    rating: Number(fd.get('rating'))
  };
  try { const data = await postJson('/api/video/rate', payload); show('res-rate', data); }
  catch (err) { show('res-rate', `Error: ${err}`); }
});

// Recommendations
bind('#form-reco', 'submit', async (e) => {
  e.preventDefault();
  const fd = new FormData(e.target);
  const userId = Number(fd.get('userId'));
  const limit = Number(fd.get('limit'));
  try { const data = await getJson(`/api/recommendations/${userId}?limit=${limit}`); show('res-reco', data); }
  catch (err) { show('res-reco', `Error: ${err}`); }
});

// Demo buttons
// Full Demo removed from UI; keep code minimal and avoid referencing missing element.

async function runSingletonTest() {
  show('res-demo-singleton', 'Running singleton test...');
  try {
    const data = await getJson('/api/demo/singleton-test');
    show('res-demo-singleton', data);
  } catch (err) {
    show('res-demo-singleton', `Error: ${err}`);
  }
}

// Primary binding (guarded)
bind('#btn-demo-singleton', 'click', (e) => { e.preventDefault(); runSingletonTest(); });
