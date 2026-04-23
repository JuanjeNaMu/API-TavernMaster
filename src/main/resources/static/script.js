// ============================================
// TAVERNMASTER - SCRIPT DEFINITIVO CORREGIDO
// ============================================

const API_BASE = '/api';
const AUTH_STORAGE_KEY = 'tm_admin_auth';
let appInicializada = false;
let personajesPorId = new Map();
let fichasPorId = new Map();
let personajesCache = [];
let personajeSeleccionadoFicha = null;
let quitarImagenEnEdicion = false;
let ataqueEditandoId = null;

// Inicialización
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', bootAplicacion);
} else {
    bootAplicacion();
}

function bootAplicacion() {
    configurarAutenticacion();
    const sesion = sessionStorage.getItem(AUTH_STORAGE_KEY);
    if (!sesion) {
        window.location.replace('/login.html');
        setTimeout(() => {
            const fallback = document.getElementById('authGuardMessage');
            if (fallback) fallback.style.display = 'block';
        }, 900);
        return;
    }
    mostrarPanelAplicacion(sesion);
    if (!appInicializada) {
        inicializar();
        appInicializada = true;
    }
}

async function inicializar() {
    await Promise.all([
        cargarJugadores(),
        cargarPersonajes(),
        cargarCampanas(),
        cargarFichasConAtaques()
    ]);
    configurarFormularios();
    cargarSelects();
}

function configurarAutenticacion() {
    document.getElementById('btnCerrarSesion')?.addEventListener('click', cerrarSesionAdmin);
    document.getElementById('btnQuitarImagenPersonaje')?.addEventListener('click', marcarQuitarImagenPersonaje);
    document.getElementById('btnEditarDesdeFicha')?.addEventListener('click', () => {
        if (!personajeSeleccionadoFicha) return;
        cerrarModal('modalFichaPersonaje');
        cargarPersonajeParaEditar(personajeSeleccionadoFicha);
    });
}

function mostrarPanelAplicacion(nombreAdmin) {
    const app = document.getElementById('appContainer');
    if (app) app.style.display = 'block';
    const adminSesion = document.getElementById('adminSesion');
    if (adminSesion) adminSesion.textContent = `Admin: ${nombreAdmin}`;
}

function cerrarSesionAdmin() {
    sessionStorage.removeItem(AUTH_STORAGE_KEY);
    window.location.href = '/login.html';
}

// Utilidades
window.cerrarModal = function(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

window.activarTab = function(tabId) {
    document.querySelectorAll('.tab-content').forEach(el => el.classList.remove('active'));
    document.querySelectorAll('.tab').forEach(el => el.classList.remove('active'));
    document.getElementById(`tab-${tabId}`)?.classList.add('active');
    document.querySelector(`.tab[data-tab="${tabId}"]`)?.classList.add('active');
}

// ============================================
// JUGADORES
// ============================================
async function cargarJugadores() {
    const response = await fetch(`${API_BASE}/jugadores`);
    const jugadores = await response.json();
    mostrarJugadores(jugadores);
    document.getElementById('statJugadores').textContent = jugadores.length;
}

function mostrarJugadores(jugadores) {
    const tbody = document.getElementById('JugadoresBody');
    if (!tbody) return;

    tbody.innerHTML = jugadores.map(j => {
        const nombre = j.nombre_jug || j.nombreJug || '-';
        const esAdmin = (j.es_admin ?? j.esAdmin) === true;
        const sexo = j.sexo || '-';
        const fechaNacRaw = j.fecha_nac || j.fechaNac;
        const fechaNac = fechaNacRaw ? new Date(fechaNacRaw).toLocaleDateString('es-ES') : '-';

        return `
        <tr>
            <td><strong>${j.id}</strong></td>
            <td><strong>${nombre}</strong></td>
            <td>${j.email || '-'}</td>
            <td>${esAdmin ? '<span class="badge badge-admin">👑 Admin</span>' : '<span class="badge badge-user">👤 Jugador</span>'}</td>
            <td>${sexo}</td>
            <td>${fechaNac}</td>
            <td>
                <div class="action-buttons">
                    <button class="btn-small btn-warning" onclick="cargarJugadorParaEditar(${j.id})">✏️</button>
                    <button class="btn-small btn-danger" onclick="borrarJugador(${j.id})">🗑️</button>
                </div>
            </td>
        </tr>
    `;
    }).join('');
}

async function insertarJugador() {
    const data = {
        nombre_jug: document.getElementById('insertJugadorNombre').value,
        password: document.getElementById('insertJugadorPassword').value,
        email: document.getElementById('insertJugadorEmail').value,
        fecha_nac: document.getElementById('insertJugadorFechaNac').value || null,
        sexo: document.getElementById('insertJugadorSexo').value || null,
        es_admin: document.getElementById('insertJugadorAdmin').value === 'true'
    };

    const response = await fetch(`${API_BASE}/jugadores`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    });

    if (response.ok) {
        alert('✅ Jugador insertado');
        document.getElementById('formInsertarJugador').reset();
        await cargarJugadores();
        await cargarSelects();
    }
}

window.cargarJugadorParaEditar = async function(id) {
    if (!id) {
        id = document.getElementById('editarJugadorId').value;
        if (!id) {
            alert('❌ Por favor, introduce un ID de jugador');
            return;
        }
    }

    const response = await fetch(`${API_BASE}/jugadores/${id}`);
    const j = await response.json();

    document.getElementById('editJugadorId').value = j.id;
    document.getElementById('editJugadorNombre').value = j.nombre_jug || j.nombreJug || '';
    document.getElementById('editJugadorEmail').value = j.email || '';
    document.getElementById('editJugadorPassword').value = '';
    const fechaNac = j.fecha_nac || j.fechaNac;
    document.getElementById('editJugadorFechaNac').value = fechaNac ? fechaNac.split('T')[0] : '';
    document.getElementById('editJugadorSexo').value = j.sexo || '';
    document.getElementById('editJugadorAdmin').value = (j.es_admin ?? j.esAdmin) ? 'true' : 'false';

    document.getElementById('modalEditarJugador').style.display = 'block';
}

async function actualizarJugador() {
    const id = document.getElementById('editJugadorId').value;
    const data = {
        nombre_jug: document.getElementById('editJugadorNombre').value,
        email: document.getElementById('editJugadorEmail').value,
        fecha_nac: document.getElementById('editJugadorFechaNac').value || null,
        sexo: document.getElementById('editJugadorSexo').value || null,
        es_admin: document.getElementById('editJugadorAdmin').value === 'true'
    };

    const password = document.getElementById('editJugadorPassword').value;
    if (password) data.password = password;

    const response = await fetch(`${API_BASE}/jugadores/${id}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    });

    if (response.ok) {
        alert('✅ Jugador actualizado');
        cerrarModal('modalEditarJugador');
        await cargarJugadores();
        await cargarSelects();
    }
}

window.borrarJugador = async function(id) {
    if (!id) {
        id = document.getElementById('editarJugadorId').value;
        if (!id) {
            alert('❌ Por favor, introduce un ID de jugador');
            return;
        }
    }

    if (!confirm(`⚠️ ¿Borrar jugador ${id}?`)) return;

    const response = await fetch(`${API_BASE}/jugadores/${id}`, {
        method: 'DELETE'
    });

    if (response.ok) {
        alert('✅ Jugador borrado');
        await cargarJugadores();
        await cargarSelects();
        document.getElementById('editarJugadorId').value = '';
    }
}

window.borrarJugadorPorId = function(id) {
    if (!id) id = document.getElementById('editarJugadorId').value;
    if (id) borrarJugador(id);
}

// ============================================
// PERSONAJES - CORREGIDO PARA USAR snake_case (id_cam, nombre_per, etc)
// ============================================
async function cargarPersonajes() {
    const response = await fetch(`${API_BASE}/personajes`);
    const personajes = await response.json();
    personajesCache = Array.isArray(personajes) ? personajes : [];
    personajesPorId = new Map(
        personajesCache.map(p => [
            p.id_per || p.id,
            p.nombre_per || p.nombrePer || '-'
        ])
    );
    console.log('📥 Personajes recibidos:', personajes);
    mostrarPersonajes(personajesCache);
    document.getElementById('statPersonajes').textContent = personajes.length;
    await cargarFichasConAtaques();
}

function mostrarPersonajes(personajes) {
    const tbody = document.getElementById('PersonajesBody');
    if (!tbody) return;

    if (!personajes?.length) {
        tbody.innerHTML = '<tr><td colspan="9" style="text-align: center;">No hay personajes</td></tr>';
        return;
    }

    tbody.innerHTML = personajes.map(p => {
        // 🟢 CORREGIDO: Usar snake_case que es lo que devuelve el backend 🟢
        const idCam = p.id_cam !== undefined && p.id_cam !== null ? p.id_cam : 'NULL';
        const idPer = p.id_per || p.id || '?';
        const nombrePer = p.nombre_per || p.nombrePer || '-';
        const clase = fichasPorId.get(idPer)?.clase || '-';
        const claseCss = obtenerClaseCss(clase);
        const nivel = p.nivel || '1';
        const jugadorPadre = p.jugador_padre || p.jugadorPadre || '-';

        const imagenSrc = normalizarImagenBase64(p.imagen_base64 || p.imagenBase64);
        const miniatura = imagenSrc
            ? `<img src="${imagenSrc}" alt="Miniatura ${nombrePer}" class="imagen-thumb">`
            : '-';

        return `
            <tr class="personaje-fila ${claseCss}">
                <td><strong>${idPer}</strong></td>
                <td><strong>${nombrePer}</strong></td>
                <td>${clase}</td>
                <td>${nivel}</td>
                <td>${jugadorPadre}</td>
                <td>${idCam}</td>
                <td>${miniatura}</td>
                <td>
                    <div class="action-buttons">
                        <button class="btn-small btn-warning" onclick="cargarPersonajeParaEditar(${idPer})">✏️</button>
                        <button class="btn-small btn-danger" onclick="borrarPersonaje(${idPer})">🗑️</button>
                    </div>
                </td>
                <td><button class="btn-small" type="button" onclick="abrirModalFicha(${idPer})">Ver ficha</button></td>
            </tr>
        `;
    }).join('');
}

function normalizarImagenBase64(valor) {
    if (!valor || typeof valor !== 'string') return null;
    const texto = valor.trim();
    if (!texto) return null;
    if (texto.startsWith('data:image/')) return texto;
    return `data:image/png;base64,${texto}`;
}

function obtenerClaseCss(clase) {
    const c = (clase || '').toLowerCase();
    if (c.includes('guerrero')) return 'clase-guerrero';
    if (c.includes('explorador')) return 'clase-explorador';
    if (c.includes('hechicero')) return 'clase-hechicero';
    return 'clase-default';
}

async function insertarPersonaje() {
    const nombrePer = document.getElementById('insertPersonajeNombre').value;
    const nivel = parseInt(document.getElementById('insertPersonajeNivel').value) || 1;
    const clase = document.getElementById('insertPersonajeClase').value?.trim() || '';
    const jugadorPadre = document.getElementById('insertPersonajeJugadorPadre').value;
    const idCam = document.getElementById('insertPersonajeIdCam').value;
    const imagenBase64 = document.getElementById('insertPersonajeImagen').value || null;

    // 🟢 CORREGIDO: Enviar con snake_case 🟢
    const data = {
        nombre_per: nombrePer,
        nivel: nivel,
        jugador_padre: jugadorPadre,
        imagen_base64: imagenBase64
    };

    if (idCam && idCam !== '') {
        data.id_cam = parseInt(idCam);
    }

    console.log('📤 Insertando personaje:', data);

    const response = await fetch(`${API_BASE}/personajes`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    });

    if (response.ok) {
        const personajeCreado = await response.json();
        const idPerCreado = personajeCreado?.id_per || personajeCreado?.id;
        if (clase && idPerCreado) {
            await actualizarClaseFicha(idPerCreado, clase);
        }
        alert('✅ Personaje insertado');
        document.getElementById('formInsertarPersonaje').reset();
        document.getElementById('insertPersonajeClase').value = '';
        await cargarPersonajes();
        if (idPerCreado) abrirModalFicha(idPerCreado);
    } else {
        const error = await response.text();
        alert('❌ Error: ' + error);
    }
}

window.cargarPersonajeParaEditar = async function(id) {
    if (!id) {
        id = document.getElementById('editarPersonajeId').value;
        if (!id) {
            alert('❌ Por favor, introduce un ID de personaje');
            return;
        }
    }

    const response = await fetch(`${API_BASE}/personajes/${id}`);
    const p = await response.json();
    console.log('📥 Personaje cargado:', p);

    // 🟢 CORREGIDO: Usar snake_case 🟢
    document.getElementById('editPersonajeId').value = p.id_per || p.id;
    document.getElementById('editPersonajeNombre').value = p.nombre_per || p.nombrePer || '';
    document.getElementById('editPersonajeNivel').value = p.nivel || 1;
    document.getElementById('editPersonajeJugadorPadre').value = p.jugador_padre || p.jugadorPadre || '';

    const idCam = p.id_cam !== undefined && p.id_cam !== null ? p.id_cam : '';
    document.getElementById('editPersonajeIdCam').value = idCam;
    quitarImagenEnEdicion = false;
    renderImagenEdicionPersonaje(p.imagen_base64 || p.imagenBase64);
    try {
        const fichaResponse = await fetch(`${API_BASE}/fichas/${p.id_per || p.id}`);
        if (fichaResponse.ok) {
            const ficha = await fichaResponse.json();
            document.getElementById('editPersonajeClase').value = ficha.clase || '';
        } else {
            document.getElementById('editPersonajeClase').value = '';
        }
    } catch (e) {
        document.getElementById('editPersonajeClase').value = '';
    }

    document.getElementById('modalEditarPersonaje').style.display = 'block';
}

async function actualizarPersonaje() {
    const id = document.getElementById('editPersonajeId').value;
    const nombrePer = document.getElementById('editPersonajeNombre').value;
    const nivel = parseInt(document.getElementById('editPersonajeNivel').value) || 1;
    const clase = document.getElementById('editPersonajeClase').value?.trim() || '';
    const jugadorPadre = document.getElementById('editPersonajeJugadorPadre').value;
    const idCam = document.getElementById('editPersonajeIdCam').value;

    // 🟢 CORREGIDO: Enviar con snake_case 🟢
    const data = {
        nombre_per: nombrePer,
        nivel: nivel,
        jugador_padre: jugadorPadre
    };

    if (idCam && idCam !== '') {
        data.id_cam = parseInt(idCam);
    }

    console.log('📤 Actualizando personaje:', data);

    const response = await fetch(`${API_BASE}/personajes/${id}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    });

    if (response.ok) {
        if (quitarImagenEnEdicion) {
            await quitarImagenPersonaje(id);
        }
        if (clase) {
            await actualizarClaseFicha(id, clase);
        }
        alert('✅ Personaje actualizado');
        cerrarModal('modalEditarPersonaje');
        await cargarPersonajes();
        abrirModalFicha(id);
    } else {
        const error = await response.text();
        alert('❌ Error: ' + error);
    }
}

function marcarQuitarImagenPersonaje() {
    quitarImagenEnEdicion = true;
    renderImagenEdicionPersonaje(null);
}

function renderImagenEdicionPersonaje(base64) {
    const preview = document.getElementById('editPersonajeImagenPreview');
    const vacia = document.getElementById('editPersonajeImagenVacia');
    if (!preview || !vacia) return;

    const src = normalizarImagenBase64(base64);
    if (!src || quitarImagenEnEdicion) {
        preview.style.display = 'none';
        preview.removeAttribute('src');
        vacia.textContent = quitarImagenEnEdicion
            ? 'La imagen se eliminará al guardar cambios.'
            : 'Este personaje no tiene imagen.';
        vacia.style.display = 'block';
        return;
    }

    preview.src = src;
    preview.style.display = 'block';
    vacia.style.display = 'none';
}

async function quitarImagenPersonaje(idPer) {
    try {
        const response = await fetch(`${API_BASE}/personajes/${idPer}/quitar-imagen`, {
            method: 'PATCH'
        });
        if (!response.ok) {
            console.warn(`No se pudo quitar la imagen del personaje ${idPer}`);
        }
    } catch (error) {
        console.error('Error quitando imagen de personaje:', error);
    }
}

window.borrarPersonaje = async function(id) {
    if (!id) {
        id = document.getElementById('editarPersonajeId').value;
        if (!id) {
            alert('❌ Por favor, introduce un ID de personaje');
            return;
        }
    }

    if (!confirm(`⚠️ ¿Borrar personaje ${id}?`)) return;

    const response = await fetch(`${API_BASE}/personajes/${id}`, {
        method: 'DELETE'
    });

    if (response.ok) {
        alert('✅ Personaje borrado');
        await cargarPersonajes();
        await cargarFichasConAtaques();
        document.getElementById('editarPersonajeId').value = '';
        if (String(personajeSeleccionadoFicha) === String(id)) {
            personajeSeleccionadoFicha = null;
            cerrarModal('modalFichaPersonaje');
        }
    } else {
        const error = await response.text();
        alert('❌ Error: ' + error);
    }
}

window.borrarPersonajePorId = function(id) {
    if (!id) id = document.getElementById('editarPersonajeId').value;
    if (id) borrarPersonaje(id);
}

// ============================================
// CAMPAÑAS
// ============================================
async function cargarCampanas() {
    const response = await fetch(`${API_BASE}/campanas`);
    const campanas = await response.json();
    console.log('📥 Campañas recibidas:', campanas);
    mostrarCampanas(campanas);
    document.getElementById('statCampanas').textContent = campanas.length;
}

function mostrarCampanas(campanas) {
    const tbody = document.getElementById('CampanasBody');
    if (!tbody) return;

    if (!campanas?.length) {
        tbody.innerHTML = '<tr><td colspan="6" style="text-align: center;">No hay campañas</td></tr>';
        return;
    }

    tbody.innerHTML = campanas.map(c => {
        const idCam = c.id_cam || c.idCam || c.id;
        return `
            <tr>
                <td><strong>${idCam}</strong></td>
                <td><strong>${c.titulo || 'Sin título'}</strong></td>
                <td>${c.master || '-'}</td>
                <td>${c.proxima_sesion || c.proximaSesion ? new Date(c.proxima_sesion || c.proximaSesion).toLocaleDateString('es-ES') : 'NULL'}</td>
                <td style="text-align: center;"><strong>${c.encuentros ?? 'NULL'}</strong></td>
                <td>
                    <div class="action-buttons">
                        <button class="btn-small btn-warning" onclick="cargarCampanaParaEditar(${idCam})">✏️</button>
                        <button class="btn-small btn-danger" onclick="borrarCampana(${idCam})">🗑️</button>
                    </div>
                </td>
            </tr>
        `;
    }).join('');
}

async function insertarCampana() {
    const data = {
        titulo: document.getElementById('insertCampanaTitulo').value,
        master: document.getElementById('insertCampanaMaster').value,
        proxima_sesion: document.getElementById('insertCampanaProximaSesion').value || null,
        encuentros: parseInt(document.getElementById('insertCampanaEncuentros').value) || 0
    };

    const response = await fetch(`${API_BASE}/campanas`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    });

    if (response.ok) {
        alert('✅ Campaña insertada');
        document.getElementById('formInsertarCampana').reset();
        await cargarCampanas();
        await cargarSelects();
    }
}

window.cargarCampanaParaEditar = async function(id) {
    if (!id) {
        id = document.getElementById('editarCampanaId').value;
        if (!id) {
            alert('❌ Por favor, introduce un ID de campaña');
            return;
        }
    }

    const response = await fetch(`${API_BASE}/campanas/${id}`);
    const c = await response.json();

    document.getElementById('editCampanaId').value = c.id_cam || c.idCam || c.id;
    document.getElementById('editCampanaTitulo').value = c.titulo || '';
    document.getElementById('editCampanaMaster').value = c.master || '';

    if (c.proxima_sesion || c.proximaSesion) {
        const fecha = new Date(c.proxima_sesion || c.proximaSesion);
        document.getElementById('editCampanaProximaSesion').value =
            `${fecha.getFullYear()}-${String(fecha.getMonth()+1).padStart(2,'0')}-${String(fecha.getDate()).padStart(2,'0')}`;
    } else {
        document.getElementById('editCampanaProximaSesion').value = '';
    }

    document.getElementById('editCampanaEncuentros').value = c.encuentros || 0;
    document.getElementById('modalEditarCampana').style.display = 'block';
}

async function actualizarCampana() {
    const id = document.getElementById('editCampanaId').value;
    const data = {
        titulo: document.getElementById('editCampanaTitulo').value,
        master: document.getElementById('editCampanaMaster').value,
        encuentros: parseInt(document.getElementById('editCampanaEncuentros').value) || 0
    };

    const proximaSesion = document.getElementById('editCampanaProximaSesion').value;
    if (proximaSesion) data.proxima_sesion = proximaSesion;

    const response = await fetch(`${API_BASE}/campanas/${id}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    });

    if (response.ok) {
        alert('✅ Campaña actualizada');
        cerrarModal('modalEditarCampana');
        await cargarCampanas();
    }
}

window.borrarCampana = async function(id) {
    if (!id) {
        id = document.getElementById('editarCampanaId').value;
        if (!id) {
            alert('❌ Por favor, introduce un ID de campaña');
            return;
        }
    }

    if (!confirm(`⚠️ ¿Borrar campaña ${id}?`)) return;

    const response = await fetch(`${API_BASE}/campanas/${id}`, {
        method: 'DELETE'
    });

    if (response.ok) {
        alert('✅ Campaña borrada');
        await cargarCampanas();
        document.getElementById('editarCampanaId').value = '';
    }
}

window.borrarCampanaPorId = function(id) {
    if (!id) id = document.getElementById('editarCampanaId').value;
    if (id) borrarCampana(id);
}

// ============================================
// FICHAS + ATAQUES
// ============================================
async function cargarFichasConAtaques() {
    try {
        const response = await fetch(`${API_BASE}/fichas/con-ataques`);
        if (!response.ok) {
            fichasPorId = new Map();
            return;
        }

        const fichas = await response.json();
        fichasPorId = new Map(
            (Array.isArray(fichas) ? fichas : []).map(f => [
                f.id_ficha,
                f
            ])
        );
        if (personajesCache.length) {
            mostrarPersonajes(personajesCache);
        }
    } catch (error) {
        console.error('Error cargando fichas con ataques:', error);
        fichasPorId = new Map();
    }
}

async function actualizarClaseFicha(idPersonaje, clase) {
    if (!idPersonaje || !clase) return;
    try {
        const response = await fetch(`${API_BASE}/fichas/${idPersonaje}`, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ clase })
        });
        if (!response.ok) {
            console.warn(`No se pudo actualizar clase de ficha para personaje ${idPersonaje}`);
        }
    } catch (error) {
        console.error('Error actualizando clase de ficha:', error);
    }
}

window.abrirModalFicha = function(idPer) {
    personajeSeleccionadoFicha = idPer;
    renderFichaSeleccionada();
    document.getElementById('modalFichaPersonaje').style.display = 'block';
}

function renderFichaSeleccionada(errorMensaje = '') {
    const titulo = document.getElementById('fichaDetalleTitulo');
    const subtitulo = document.getElementById('fichaDetalleSubtitulo');
    const body = document.getElementById('fichaDetalleBody');
    if (!titulo || !body || !subtitulo) return;

    if (errorMensaje) {
        titulo.textContent = 'Ficha del personaje';
        subtitulo.textContent = '';
        body.className = 'ficha-vacio';
        body.textContent = errorMensaje;
        ataqueEditandoId = null;
        return;
    }

    if (!personajeSeleccionadoFicha) {
        titulo.textContent = 'Ficha del personaje';
        subtitulo.textContent = '';
        body.className = 'ficha-vacio';
        body.textContent = 'Pulsa el botón "Ver ficha" en la tabla de personajes.';
        ataqueEditandoId = null;
        return;
    }

    const idPer = personajeSeleccionadoFicha;
    const ficha = fichasPorId.get(idPer);
    const nombrePersonaje = personajesPorId.get(idPer) || `ID ${idPer}`;

    titulo.textContent = `Ficha de ${nombrePersonaje}`;
    subtitulo.textContent = `ID Personaje: ${idPer}`;

    if (!ficha) {
        body.className = 'ficha-vacio';
        body.textContent = `No hay ficha para el personaje ${nombrePersonaje}.`;
        ataqueEditandoId = null;
        return;
    }

    const ataques = Array.isArray(ficha.ataques) ? ficha.ataques : [];
    const ataquesHtml = ataques.length
        ? ataques.map(a => {
            const nombre = a.nombre || 'Sin nombre';
            const caracteristica = a.caracteristica || '-';
            const esCompetente = a.es_competente === true;
            const estadoClase = esCompetente ? 'si' : 'no';
            const estadoTexto = esCompetente ? 'Competente' : 'NoCompetente';
            return `
                <div class="ataque-admin-item">
                    <div class="ataque-admin-top">
                        <div>
                            <strong>${nombre}</strong>
                            (<span class="ataque-atributo">${caracteristica}</span>)
                            <span class="estado-comp ${estadoClase}">${estadoTexto}</span>
                        </div>
                        <div class="ataque-admin-actions">
                            <button type="button" class="btn-small btn-warning" onclick="editarAtaqueEnFicha(${a.id_ataque})">Editar</button>
                            <button type="button" class="btn-small btn-danger" onclick="borrarAtaqueEnFicha(${a.id_ataque})">Borrar</button>
                        </div>
                    </div>
                </div>
            `;
        }).join('')
        : '<span class="ficha-vacio">Sin ataques</span>';

    body.className = '';
    body.innerHTML = `
        <div class="ficha-layout">
            <div class="ficha-box">
                <h4>Datos de ficha</h4>
                <div><strong>ID ficha:</strong> ${ficha.id_ficha ?? '-'}</div>
                <div><strong>Clase:</strong> ${ficha.clase || '-'}</div>
                <div class="ficha-detalle-grid">
                    <div class="ficha-stat"><strong>Fuerza</strong>${ficha.fuerza ?? '-'}</div>
                    <div class="ficha-stat"><strong>Destreza</strong>${ficha.destreza ?? '-'}</div>
                    <div class="ficha-stat"><strong>Constitución</strong>${ficha.constitucion ?? '-'}</div>
                    <div class="ficha-stat"><strong>Inteligencia</strong>${ficha.inteligencia ?? '-'}</div>
                    <div class="ficha-stat"><strong>Sabiduría</strong>${ficha.sabiduria ?? '-'}</div>
                    <div class="ficha-stat"><strong>Carisma</strong>${ficha.carisma ?? '-'}</div>
                </div>
            </div>
            <div class="ficha-box">
                <h4>Ataques</h4>
                ${ataquesHtml}
                <h4 style="margin-top:14px;">${ataqueEditandoId ? 'Editar ataque' : 'Añadir ataque'}</h4>
                <div class="ataque-form-grid">
                    <div>
                        <label for="atkNombreInput">Nombre</label>
                        <input id="atkNombreInput" type="text" placeholder="Ej: Espadon">
                    </div>
                    <div>
                        <label for="atkCaracInput">Atributo</label>
                        <select id="atkCaracInput">
                            <option value="Fuerza">Fuerza</option>
                            <option value="Destreza">Destreza</option>
                            <option value="Constitucion">Constitucion</option>
                            <option value="Inteligencia">Inteligencia</option>
                            <option value="Sabiduria">Sabiduria</option>
                            <option value="Carisma">Carisma</option>
                        </select>
                    </div>
                    <div>
                        <label for="atkCompInput">Competencia</label>
                        <select id="atkCompInput">
                            <option value="true">Competente</option>
                            <option value="false">NoCompetente</option>
                        </select>
                    </div>
                    <div>
                        <button type="button" class="btn-success" onclick="guardarAtaqueDesdeFicha()">${ataqueEditandoId ? 'Guardar' : 'Anadir'}</button>
                        ${ataqueEditandoId ? '<button type="button" class="btn-small" style="margin-top:6px;" onclick="cancelarEdicionAtaque()">Cancelar</button>' : ''}
                    </div>
                </div>
            </div>
        </div>
    `;

    if (ataqueEditandoId) {
        const atk = ataques.find(a => String(a.id_ataque) === String(ataqueEditandoId));
        if (atk) {
            const nombreInput = document.getElementById('atkNombreInput');
            const caracInput = document.getElementById('atkCaracInput');
            const compInput = document.getElementById('atkCompInput');
            if (nombreInput) nombreInput.value = atk.nombre || '';
            if (caracInput) caracInput.value = atk.caracteristica || 'Fuerza';
            if (compInput) compInput.value = String(atk.es_competente === true);
        }
    }
}

window.editarAtaqueEnFicha = function(idAtaque) {
    ataqueEditandoId = idAtaque;
    renderFichaSeleccionada();
}

window.cancelarEdicionAtaque = function() {
    ataqueEditandoId = null;
    renderFichaSeleccionada();
}

window.guardarAtaqueDesdeFicha = async function() {
    if (!personajeSeleccionadoFicha) return;
    const nombre = document.getElementById('atkNombreInput')?.value?.trim();
    const caracteristica = document.getElementById('atkCaracInput')?.value || 'Fuerza';
    const esCompetente = (document.getElementById('atkCompInput')?.value || 'true') === 'true';

    if (!nombre) {
        alert('El nombre del ataque es obligatorio.');
        return;
    }

    const payload = {
        id_per: personajeSeleccionadoFicha,
        nombre,
        caracteristica,
        es_competente: esCompetente
    };

    const endpoint = ataqueEditandoId ? `${API_BASE}/ataques/${ataqueEditandoId}` : `${API_BASE}/ataques`;
    const method = ataqueEditandoId ? 'PUT' : 'POST';

    try {
        const response = await fetch(endpoint, {
            method,
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(payload)
        });
        if (!response.ok) {
            const errorText = await response.text();
            alert(`No se pudo guardar el ataque: ${errorText}`);
            return;
        }
        ataqueEditandoId = null;
        await cargarFichasConAtaques();
        renderFichaSeleccionada();
    } catch (e) {
        alert('Error de red al guardar ataque.');
    }
}

window.borrarAtaqueEnFicha = async function(idAtaque) {
    if (!confirm('¿Seguro que quieres borrar este ataque?')) return;
    try {
        const response = await fetch(`${API_BASE}/ataques/${idAtaque}`, { method: 'DELETE' });
        if (!response.ok) {
            const errorText = await response.text();
            alert(`No se pudo borrar el ataque: ${errorText}`);
            return;
        }
        if (String(ataqueEditandoId) === String(idAtaque)) {
            ataqueEditandoId = null;
        }
        await cargarFichasConAtaques();
        renderFichaSeleccionada();
    } catch (e) {
        alert('Error de red al borrar ataque.');
    }
}

// ============================================
// SELECTS
// ============================================
async function cargarSelects() {
    try {
        const response = await fetch(`${API_BASE}/jugadores`);
        const jugadores = await response.json();
        const options = jugadores
            .map(j => j.nombre_jug || j.nombreJug)
            .filter(Boolean)
            .map(nombre => `<option value="${nombre}">`)
            .join('');

        const datalist1 = document.getElementById('jugadores-list');
        if (datalist1) datalist1.innerHTML = options;

        const datalist2 = document.getElementById('jugadores-list-edit');
        if (datalist2) datalist2.innerHTML = options;
    } catch (error) {
        console.error('Error:', error);
    }
}

// Configurar formularios
function configurarFormularios() {
    document.getElementById('formInsertarJugador')?.addEventListener('submit', e => { e.preventDefault(); insertarJugador(); });
    document.getElementById('formInsertarPersonaje')?.addEventListener('submit', e => { e.preventDefault(); insertarPersonaje(); });
    document.getElementById('formInsertarCampana')?.addEventListener('submit', e => { e.preventDefault(); insertarCampana(); });
    document.getElementById('formEditarJugador')?.addEventListener('submit', e => { e.preventDefault(); actualizarJugador(); });
    document.getElementById('formEditarPersonaje')?.addEventListener('submit', e => { e.preventDefault(); actualizarPersonaje(); });
    document.getElementById('formEditarCampana')?.addEventListener('submit', e => { e.preventDefault(); actualizarCampana(); });
}