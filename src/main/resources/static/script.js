// ESPERAR ANTES DE HACER NADA, QUE CARGUE LA PAGINA
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', inicializar);
} else {
    inicializar();
}

// FUNCIÓN PRINCIPAL DE INICIALIZACIÓN
function inicializar() {
    console.log("Inicializando aplicación...");
    cargarEnemigos();        // TRAE LOS ENEMIGOS DE LA BASE DE DATOS
    configurarFormularios(); // CONFIGURA LOS LISTENERS DE LOS FORMULARIOS
}

// CONFIGURACIÓN DE FORMULARIOS
function configurarFormularios() {
    console.log("Configurando formularios...");

    // FORMULARIO INSERTAR
    const formInsertar = document.getElementById('formInsertar');
    if (formInsertar) {
        formInsertar.addEventListener('submit', function(e) {
            e.preventDefault(); // NO RECARGUE LA PÁGINA
            e.stopPropagation(); // NO PROPAGUE EL EVENTO
            console.log("SUBMIT INSERTAR");
            insertarEnemigo(); // LLAMA A LA FUNCIÓN DE INSERTAR
        });
        console.log("✓ Listener INSERTAR configurado");
    } else {
        console.error("✗ No se encontró formInsertar");
    }

    // FORMULARIO EDITAR
    const formEditar = document.getElementById('formEditar');
    if (formEditar) {
        formEditar.addEventListener('submit', function(e) {
            e.preventDefault();
            e.stopPropagation();
            console.log("SUBMIT EDITAR");
            editarEnemigo(); // LLAMA A LA FUNCIÓN DE EDITAR
        });
        console.log("✓ Listener EDITAR configurado");
    } else {
        console.error("✗ No se encontró formEditar");
    }

    // FORMULARIO ELIMINAR
    const formEliminar = document.getElementById('formEliminar');
    if (formEliminar) {
        formEliminar.addEventListener('submit', function(e) {
            e.preventDefault();
            e.stopPropagation();
            console.log("SUBMIT ELIMINAR");
            eliminarEnemigo(); // LLAMA A LA FUNCIÓN DE ELIMINAR
        });
        console.log("✓ Listener ELIMINAR configurado");
    } else {
        console.error("✗ No se encontró formEliminar");
    }
}

// CARGAR ENEMIGOS DESDE EL SERVIDOR
async function cargarEnemigos() {
    console.log("Cargando enemigos...");
    try {
        const response = await fetch('api/enemigos'); // PEDIDO GET A LA API
        if (!response.ok) throw new Error('Error HTTP: ' + response.status);
        const enemigos = await response.json(); // CONVIERTE LA RESPUESTA EN JSON
        console.log("Enemigos cargados:", enemigos.length);
        mostrarEnemigos(enemigos); // MUESTRA EN LA TABLA
    } catch (error) {
        console.error("Error al cargar enemigos:", error);
    }
}

// MOSTRAR ENEMIGOS EN LA TABLA
function mostrarEnemigos(enemigos) {
    const tbody = document.getElementById('EnemigosBody');
    const table = document.getElementById('EnemigosTable');

    tbody.innerHTML = ''; // LIMPIA LA TABLA

    if (enemigos.length === 0) {
        console.log("No hay enemigos para mostrar");
        return;
    }

    enemigos.forEach(enemigo => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${enemigo.id}</td>
            <td>${enemigo.name}</td>
            <td>${enemigo.pais}</td>
            <td>${enemigo.afiliacion_politica}</td>
        `;
        tbody.appendChild(tr);
    });

    table.style.display = 'table';
    console.log("Tabla actualizada con " + enemigos.length + " enemigos");
}

// INSERTAR NUEVO ENEMIGO
async function insertarEnemigo() {
    console.log("=== INSERTAR ENEMIGO ===");

    // TOMAR LOS DATOS DEL FORMULARIO
    const name = document.getElementById('insertName').value.trim();
    const pais = document.getElementById('insertPais').value.trim();
    const afiliacion_politica = document.getElementById('insertAfiliacion_politica').value.trim();

    if (!name || !pais || !afiliacion_politica) {
        alert('Por favor completa todos los campos');
        return;
    }

    const nuevoEnemigo = { name, pais, afiliacion_politica };

    try {
        const response = await fetch('api/enemigos', {
            method: 'POST', // PETICIÓN POST PARA CREAR
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(nuevoEnemigo)
        });

        console.log("Status:", response.status);

        if (response.ok) {
            const data = await response.json();
            console.log("Enemigo creado:", data);
            alert('✓ Enemigo insertado correctamente (ID: ' + data.id + ')');
            document.getElementById('formInsertar').reset();
            await cargarEnemigos(); // ACTUALIZA LA TABLA
        } else {
            const errorText = await response.text();
            console.error("Error del servidor:", errorText);
            alert('✗ Error al insertar: ' + errorText);
        }
    } catch (error) {
        console.error('Error:', error);
        alert('✗ Error de red: ' + error.message);
    }
}

// EDITAR ENEMIGO EXISTENTE
async function editarEnemigo() {
    console.log("=== EDITAR ENEMIGO ===");

    const id = document.getElementById('editId').value.trim();
    const name = document.getElementById('editName').value.trim();
    const pais = document.getElementById('editPais').value.trim();
    const afiliacion_politica = document.getElementById('editAfiliacion_politica').value.trim();

    if (!id || !name || !pais || !afiliacion_politica) {
        alert('Por favor completa todos los campos');
        return;
    }

    const enemigoActualizado = { name, pais, afiliacion_politica };

    try {
        const response = await fetch(`api/enemigos/${id}`, {
            method: 'PUT', // PETICIÓN PUT PARA ACTUALIZAR
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(enemigoActualizado)
        });

        console.log("Status:", response.status);

        if (response.ok) {
            const data = await response.json();
            console.log("Enemigo actualizado:", data);
            alert('✓ Enemigo actualizado correctamente');
            document.getElementById('formEditar').reset();
            await cargarEnemigos();
        } else {
            const errorText = await response.text();
            console.error("Error del servidor:", errorText);
            alert('✗ Error al actualizar: ' + (response.status === 404 ? 'ID no encontrado' : errorText));
        }
    } catch (error) {
        console.error('Error:', error);
        alert('✗ Error de red: ' + error.message);
    }
}

// ELIMINAR ENEMIGO
async function eliminarEnemigo() {
    console.log("=== ELIMINAR ENEMIGO ===");

    const id = document.getElementById('deleteId').value.trim();

    if (!id) {
        alert('Por favor ingresa un ID');
        return;
    }

    if (!confirm('¿Estás seguro de eliminar el enemigo con ID ' + id + '?')) {
        console.log("Eliminación cancelada");
        return;
    }

    try {
        const response = await fetch(`api/enemigos/${id}`, { method: 'DELETE' }); // PETICIÓN DELETE

        console.log("Status:", response.status);

        if (response.ok) {
            console.log("Enemigo eliminado exitosamente");
            alert('✓ Enemigo eliminado correctamente');
            document.getElementById('formEliminar').reset();
            await cargarEnemigos(); // ACTUALIZA LA TABLA
        } else {
            console.error("Error al eliminar, status:", response.status);
            alert('✗ Error al eliminar: ' + (response.status === 404 ? 'ID no encontrado' : 'Error del servidor'));
        }
    } catch (error) {
        console.error('Error:', error);
        alert('✗ Error de red: ' + error.message);
    }
}