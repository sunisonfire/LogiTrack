(() => {
  "use strict";

  const CONFIG = window.LOGITRACK_CONFIG || {};
  const API_BASE = String(CONFIG.API_BASE_URL || "http://localhost:8080").replace(/\/+$/, "");
  const TIMEOUT = Number(CONFIG.REQUEST_TIMEOUT_MS || 15000);
  const state = { token: null, user: null, admins: [], people: [], deleteId: null };

  const $ = (selector, root = document) => root.querySelector(selector);
  const $$ = (selector, root = document) => [...root.querySelectorAll(selector)];

  function toast(message, type = "success") {
    const node = document.createElement("div");
    node.className = `toast ${type}`;
    node.textContent = message;
    $("#toast-region").append(node);
    setTimeout(() => node.remove(), 3800);
  }

  function safe(value) {
    return String(value ?? "").replace(/[&<>"']/g, c => ({
      "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;"
    })[c]);
  }

  async function api(path, options = {}) {
    const controller = new AbortController();
    const timer = setTimeout(() => controller.abort(), TIMEOUT);
    const headers = new Headers(options.headers || {});
    headers.set("Accept", "application/json");
    if (options.body && !(options.body instanceof FormData)) headers.set("Content-Type", "application/json");
    if (state.token) headers.set("Authorization", `Bearer ${state.token}`);
    try {
      const response = await fetch(`${API_BASE}${path}`, {
        ...options, headers, signal: controller.signal
      });
      const raw = await response.text();
      let data = null;
      if (raw) {
        try { data = JSON.parse(raw); } catch { data = raw; }
      }
      if (!response.ok) {
        if (response.status === 401 && state.token) logout(false);
        const detail = typeof data === "string" ? data : (data?.message || data?.error || data?.detail);
        throw new Error(detail || `Error HTTP ${response.status}`);
      }
      return data;
    } catch (error) {
      if (error.name === "AbortError") throw new Error("La solicitud tardó demasiado. Comprueba el servidor.");
      if (error instanceof TypeError) throw new Error(`No se pudo conectar con ${API_BASE}. Verifica el puerto y CORS.`);
      throw error;
    } finally {
      clearTimeout(timer);
    }
  }

  async function testConnection() {
    const label = $("#connection-label");
    label.textContent = "Comprobando...";
    try {
      // Endpoint público de autenticación: se envía un OPTIONS para comprobar CORS/red.
      const response = await fetch(`${API_BASE}/auth/login`, {
        method: "OPTIONS", headers: { "Accept": "application/json" }
      });
      if (response.ok || response.status === 401 || response.status === 403 || response.status === 405) {
        label.textContent = `Servidor disponible · ${API_BASE}`;
        $("#stat-api").textContent = "Activo";
        $("#stat-api-detail").textContent = API_BASE;
        toast("El servidor respondió. La conexión de red está disponible.");
      } else {
        label.textContent = `Respuesta HTTP ${response.status}`;
        throw new Error(`El servidor respondió HTTP ${response.status}`);
      }
    } catch (error) {
      label.textContent = "Sin conexión con la API";
      $("#stat-api").textContent = "Inactivo";
      $("#stat-api-detail").textContent = "Revisa API_BASE_URL y CORS";
      toast(error.message, "error");
    }
  }

  function showLogin() {
    $("#login-view").classList.remove("hidden");
    $("#app-view").classList.add("hidden");
  }

  function showApp() {
    $("#login-view").classList.add("hidden");
    $("#app-view").classList.remove("hidden");
    const name = state.user?.nombre || "bienvenido";
    $("#user-name").textContent = name;
    $("#hello-name").textContent = name.split(" ")[0];
    $("#user-role").textContent = state.user?.rol || "";
    $("#user-avatar").textContent = name.trim().charAt(0).toUpperCase() || "L";
    const isAdmin = state.user?.rol === "ADMIN";
    $$('[data-page="administradores"], [data-page="personas"]').forEach(el => {
      el.classList.toggle("hidden", !isAdmin);
    });
    navigate("overview");
    loadOverview();
  }

  function logout(showMessage = true) {
    state.token = null;
    state.user = null;
    sessionStorage.removeItem("logitrack-session");
    showLogin();
    if (showMessage) toast("Sesión cerrada correctamente.");
  }

  async function handleLogin(event) {
    event.preventDefault();
    const form = new FormData(event.currentTarget);
    const errorBox = $("#login-error");
    const button = $('button[type="submit"]', event.currentTarget);
    errorBox.textContent = "";
    button.disabled = true;
    button.textContent = "Ingresando...";
    try {
      const data = await api("/auth/login", {
        method: "POST",
        body: JSON.stringify({
          username: form.get("username").trim(),
          password: form.get("password"),
          rol: form.get("rol")
        })
      });
      state.token = data.token;
      state.user = {
        rol: data.rol,
        personaId: data.personaId,
        nombre: data.nombre,
        bodegaId: data.bodegaId
      };
      sessionStorage.setItem("logitrack-session", JSON.stringify({ token: state.token, user: state.user }));
      showApp();
      toast(`Bienvenido${data.nombre ? `, ${data.nombre}` : ""}.`);
    } catch (error) {
      errorBox.textContent = error.message;
    } finally {
      button.disabled = false;
      button.innerHTML = 'Entrar <span>→</span>';
    }
  }

  function navigate(page) {
    $$(".page").forEach(el => el.classList.add("hidden"));
    $(`#page-${page}`)?.classList.remove("hidden");
    $$(".nav-item[data-page]").forEach(el => el.classList.toggle("active", el.dataset.page === page));
    const labels = { overview: "RESUMEN", administradores: "ADMINISTRADORES", personas: "PERSONAS" };
    const titles = { overview: "Resumen general", administradores: "Administradores", personas: "Personas" };
    $("#breadcrumb").textContent = labels[page] || "LOGITRACK";
    $("#welcome-title").textContent = titles[page] || "LogiTrack";
    $("#sidebar").classList.remove("open");
    if (page === "administradores") loadAdmins();
    if (page === "personas") loadPeople();
  }

  async function loadOverview() {
    if (state.user?.rol !== "ADMIN") {
      $("#stat-admins").textContent = "—";
      $("#stat-people").textContent = "—";
      $("#stat-api").textContent = "Conectado";
      return;
    }
    try {
      const [admins, people] = await Promise.all([
        api("/api/administrador"), api("/api/persona")
      ]);
      state.admins = Array.isArray(admins) ? admins : [];
      state.people = Array.isArray(people) ? people : [];
      $("#stat-admins").textContent = state.admins.length;
      $("#stat-people").textContent = state.people.length;
      $("#stat-api").textContent = "Activo";
      $("#stat-api-detail").textContent = "Datos recibidos correctamente";
    } catch (error) {
      $("#stat-admins").textContent = "—";
      $("#stat-people").textContent = "—";
      $("#stat-api").textContent = "Revisar";
      $("#stat-api-detail").textContent = error.message;
    }
  }

  function adminRows(items) {
    if (!items.length) return '<tr><td colspan="5" class="empty-cell">No hay administradores para mostrar.</td></tr>';
    return items.map(item => {
      const p = item.persona || {};
      const name = `${p.nombre || ""} ${p.apellido || ""}`.trim() || "Sin nombre";
      return `<tr>
        <td><div class="person-cell"><span class="table-avatar">${safe(name.charAt(0).toUpperCase())}</span><b>${safe(name)}</b></div></td>
        <td>${safe(p.documento || "—")}</td><td>${safe(item.username || "—")}</td><td>#${safe(item.id)}</td>
        <td><div class="row-actions"><button class="action-button" data-edit-admin="${safe(item.id)}">Editar</button><button class="action-button danger-text" data-delete-admin="${safe(item.id)}">Eliminar</button></div></td>
      </tr>`;
    }).join("");
  }

  async function loadAdmins() {
    const body = $("#admins-body");
    body.innerHTML = '<tr><td colspan="5" class="empty-cell">Cargando...</td></tr>';
    try {
      const data = await api("/api/administrador");
      state.admins = Array.isArray(data) ? data : [];
      renderAdmins();
      $("#stat-admins").textContent = state.admins.length;
    } catch (error) {
      body.innerHTML = `<tr><td colspan="5" class="empty-cell error-text">${safe(error.message)}</td></tr>`;
    }
  }

  function renderAdmins() {
    const term = $("#admin-search").value.trim().toLowerCase();
    const filtered = state.admins.filter(a => {
      const p = a.persona || {};
      return [p.nombre, p.apellido, p.documento, a.username, a.id].join(" ").toLowerCase().includes(term);
    });
    $("#admins-body").innerHTML = adminRows(filtered);
  }

  async function loadPeople() {
    const body = $("#people-body");
    body.innerHTML = '<tr><td colspan="4" class="empty-cell">Cargando...</td></tr>';
    try {
      const data = await api("/api/persona");
      state.people = Array.isArray(data) ? data : [];
      renderPeople();
      $("#stat-people").textContent = state.people.length;
    } catch (error) {
      body.innerHTML = `<tr><td colspan="4" class="empty-cell error-text">${safe(error.message)}</td></tr>`;
    }
  }

  function renderPeople() {
    const term = $("#people-search").value.trim().toLowerCase();
    const filtered = state.people.filter(p =>
      [p.nombre, p.apellido, p.documento, p.tipoPersona, p.id].join(" ").toLowerCase().includes(term));
    $("#people-body").innerHTML = filtered.length ? filtered.map(p => `
      <tr><td><div class="person-cell"><span class="table-avatar">${safe((p.nombre || "?").charAt(0).toUpperCase())}</span><b>${safe(`${p.nombre || ""} ${p.apellido || ""}`.trim())}</b></div></td>
      <td>${safe(p.documento || "—")}</td><td><span class="role-pill">${safe(p.tipoPersona || "—")}</span></td><td>#${safe(p.id)}</td></tr>
    `).join("") : '<tr><td colspan="4" class="empty-cell">No hay personas para mostrar.</td></tr>';
  }

  function openAdminDialog(admin = null) {
    const form = $("#admin-form");
    form.reset();
    $("#admin-form-error").textContent = "";
    $("#admin-dialog-title").textContent = admin ? "Editar administrador" : "Nuevo administrador";
    $("#password-hint").textContent = admin ? "Deja la contraseña vacía para conservar la actual." : "La contraseña es obligatoria para crear una cuenta.";
    form.elements.id.value = admin?.id || "";
    form.elements.nombre.value = admin?.persona?.nombre || "";
    form.elements.apellido.value = admin?.persona?.apellido || "";
    form.elements.documento.value = admin?.persona?.documento || "";
    form.elements.username.value = admin?.username || "";
    form.elements.password.required = !admin;
    $("#admin-dialog").showModal();
  }

  async function saveAdmin(event) {
    event.preventDefault();
    const form = event.currentTarget;
    const values = new FormData(form);
    const id = values.get("id");
    const password = values.get("password");
    const payload = {
      persona: {
        nombre: values.get("nombre").trim(),
        apellido: values.get("apellido").trim(),
        documento: values.get("documento").trim()
      },
      username: values.get("username").trim()
    };
    if (password) payload.password = password;
    const button = $("#save-admin");
    button.disabled = true;
    $("#admin-form-error").textContent = "";
    try {
      await api(id ? `/api/administrador/${encodeURIComponent(id)}` : "/api/administrador", {
        method: id ? "PUT" : "POST", body: JSON.stringify(payload)
      });
      $("#admin-dialog").close();
      toast(id ? "Administrador actualizado." : "Administrador creado.");
      await loadAdmins();
      await loadOverview();
    } catch (error) {
      $("#admin-form-error").textContent = error.message;
    } finally {
      button.disabled = false;
    }
  }

  function askDelete(id) {
    state.deleteId = id;
    $("#confirm-dialog").showModal();
  }

  async function deleteAdmin(event) {
    if (event.target.value !== "confirm" || !state.deleteId) return;
    const id = state.deleteId;
    state.deleteId = null;
    try {
      await api(`/api/administrador/${encodeURIComponent(id)}`, { method: "DELETE" });
      toast("Administrador eliminado.");
      await loadAdmins();
      await loadOverview();
    } catch (error) {
      toast(error.message, "error");
    }
  }

  function restoreSession() {
    try {
      const saved = JSON.parse(sessionStorage.getItem("logitrack-session") || "null");
      if (saved?.token && saved?.user) {
        state.token = saved.token;
        state.user = saved.user;
        showApp();
      }
    } catch { sessionStorage.removeItem("logitrack-session"); }
  }

  document.addEventListener("DOMContentLoaded", () => {
    $("#login-form").addEventListener("submit", handleLogin);
    $("#test-api").addEventListener("click", testConnection);
    $("#logout-button").addEventListener("click", () => logout());
    $$(".nav-item[data-page]").forEach(button => button.addEventListener("click", () => navigate(button.dataset.page)));
    $("#new-admin").addEventListener("click", () => openAdminDialog());
    $("#refresh-admins").addEventListener("click", loadAdmins);
    $("#refresh-people").addEventListener("click", loadPeople);
    $("#admin-search").addEventListener("input", renderAdmins);
    $("#people-search").addEventListener("input", renderPeople);
    $("#admin-form").addEventListener("submit", saveAdmin);
    $("#admins-body").addEventListener("click", event => {
      const edit = event.target.closest("[data-edit-admin]");
      const del = event.target.closest("[data-delete-admin]");
      if (edit) openAdminDialog(state.admins.find(a => String(a.id) === edit.dataset.editAdmin));
      if (del) askDelete(del.dataset.deleteAdmin);
    });
    $("#confirm-dialog").addEventListener("close", deleteAdmin);
    $("#close-admin-dialog").addEventListener("click", () => $("#admin-dialog").close());
    $("#cancel-admin").addEventListener("click", () => $("#admin-dialog").close());
    $("#menu-toggle").addEventListener("click", () => $("#sidebar").classList.toggle("open"));
    restoreSession();
  });
})();
