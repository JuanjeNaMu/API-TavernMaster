// ============================================
// TAVERNMASTER - SCRIPT DEFINITIVO CORREGIDO
// ============================================

const API_BASE = '/api';

// Inicialización
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', inicializar);
} else {
    inicializar();
}

async function inicializar() {
    await Promise.all([
        cargarJugadores(),
        cargarPersonajes(),
        cargarCampanas()
    ]);
    configurarFormularios();
    cargarSelects();
}

// Utilidades
window.cerrarModal = function(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

window.activarTab = function(tabId) {
    document.querySelectorAll('.tab-content').forEach(el => el.classList.remove('active'));
    document.querySelectorAll('.tab').forEach(el => el.classList.remove('active'));
    document.getElementById(`tab-${tabId}`)?.classList.add('active');

    document.querySelectorAll('.tab').forEach(btn => {
        if (btn.textContent.includes(tabId === 'jugadores' ? 'Jugadores' :
                                      tabId === 'personajes' ? 'Personajes' : 'Campañas')) {
            btn.classList.add('active');
        }
    });
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

    tbody.innerHTML = jugadores.map(j => `
        <tr>
            <td><strong>${j.id}</strong></td>
            <td><strong>${j.nombreJug || '-'}</strong></td>
            <td>${j.email || '-'}</td>
            <td>${j.esAdmin ? '<span class="badge badge-admin">👑 Admin</span>' : '<span class="badge badge-user">👤 Jugador</span>'}</td>
            <td>${j.sexo || '-'}</td>
            <td>${j.fechaNac ? new Date(j.fechaNac).toLocaleDateString('es-ES') : '-'}</td>
            <td>
                <div class="action-buttons">
                    <button class="btn-small btn-warning" onclick="cargarJugadorParaEditar(${j.id})">✏️</button>
                    <button class="btn-small btn-danger" onclick="borrarJugador(${j.id})">🗑️</button>
                </div>
            </td>
        </tr>
    `).join('');
}

async function insertarJugador() {
    const data = {
        nombreJug: document.getElementById('insertJugadorNombre').value,
        password: document.getElementById('insertJugadorPassword').value,
        email: document.getElementById('insertJugadorEmail').value,
        fechaNac: document.getElementById('insertJugadorFechaNac').value || null,
        sexo: document.getElementById('insertJugadorSexo').value || null,
        esAdmin: document.getElementById('insertJugadorAdmin').value === 'true'
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
    document.getElementById('editJugadorNombre').value = j.nombreJug || '';
    document.getElementById('editJugadorEmail').value = j.email || '';
    document.getElementById('editJugadorPassword').value = '';
    document.getElementById('editJugadorFechaNac').value = j.fechaNac ? j.fechaNac.split('T')[0] : '';
    document.getElementById('editJugadorSexo').value = j.sexo || '';
    document.getElementById('editJugadorAdmin').value = j.esAdmin ? 'true' : 'false';

    document.getElementById('modalEditarJugador').style.display = 'block';
}

async function actualizarJugador() {
    const id = document.getElementById('editJugadorId').value;
    const data = {
        nombreJug: document.getElementById('editJugadorNombre').value,
        email: document.getElementById('editJugadorEmail').value,
        fechaNac: document.getElementById('editJugadorFechaNac').value || null,
        sexo: document.getElementById('editJugadorSexo').value || null,
        esAdmin: document.getElementById('editJugadorAdmin').value === 'true'
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
    console.log('📥 Personajes recibidos:', personajes);
    mostrarPersonajes(personajes);
    document.getElementById('statPersonajes').textContent = personajes.length;
}

function mostrarPersonajes(personajes) {
    const tbody = document.getElementById('PersonajesBody');
    if (!tbody) return;

    if (!personajes?.length) {
        tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">No hay personajes</td></tr>';
        return;
    }

    tbody.innerHTML = personajes.map(p => {
        // 🟢 CORREGIDO: Usar snake_case que es lo que devuelve el backend 🟢
        const idCam = p.id_cam !== undefined && p.id_cam !== null ? p.id_cam : 'NULL';
        const idPer = p.id_per || p.id || '?';
        const nombrePer = p.nombre_per || p.nombrePer || '-';
        const nivel = p.nivel || '1';
        const jugadorPadre = p.jugador_padre || p.jugadorPadre || '-';

        return `
            <tr>
                <td><strong>${idPer}</strong></td>
                <td><strong>${nombrePer}</strong></td>
                <td>${nivel}</td>
                <td>${jugadorPadre}</td>
                <td>${idCam}</td>
                <td>${p.imagen_base64 || p.imagenBase64 ? '🖼️' : '-'}</td>
                <td>
                    <div class="action-buttons">
                        <button class="btn-small btn-warning" onclick="cargarPersonajeParaEditar(${idPer})">✏️</button>
                        <button class="btn-small btn-danger" onclick="borrarPersonaje(${idPer})">🗑️</button>
                    </div>
                </td>
            </tr>
        `;
    }).join('');
}

async function insertarPersonaje() {
    const nombrePer = document.getElementById('insertPersonajeNombre').value;
    const nivel = parseInt(document.getElementById('insertPersonajeNivel').value) || 1;
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
        alert('✅ Personaje insertado');
        document.getElementById('formInsertarPersonaje').reset();
        await cargarPersonajes();
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

    document.getElementById('editPersonajeImagen').value = p.imagen_base64 || p.imagenBase64 || '';

    document.getElementById('modalEditarPersonaje').style.display = 'block';
}

async function actualizarPersonaje() {
    const id = document.getElementById('editPersonajeId').value;
    const nombrePer = document.getElementById('editPersonajeNombre').value;
    const nivel = parseInt(document.getElementById('editPersonajeNivel').value) || 1;
    const jugadorPadre = document.getElementById('editPersonajeJugadorPadre').value;
    const idCam = document.getElementById('editPersonajeIdCam').value;
    const imagenBase64 = document.getElementById('editPersonajeImagen').value || null;

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

    console.log('📤 Actualizando personaje:', data);

    const response = await fetch(`${API_BASE}/personajes/${id}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    });

    if (response.ok) {
        alert('✅ Personaje actualizado');
        cerrarModal('modalEditarPersonaje');
        await cargarPersonajes();
    } else {
        const error = await response.text();
        alert('❌ Error: ' + error);
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
        document.getElementById('editarPersonajeId').value = '';
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
// SELECTS
// ============================================
async function cargarSelects() {
    try {
        const response = await fetch(`${API_BASE}/jugadores`);
        const jugadores = await response.json();
        const options = jugadores.map(j => `<option value="${j.nombreJug}">`).join('');

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