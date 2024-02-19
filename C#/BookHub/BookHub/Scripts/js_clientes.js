function getParameterByName(name) {
    if (name !== "" && name !== null && name != undefined) {
        name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
        var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
        return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
    } else {
        var arr = location.href.split("/");
        return arr[arr.length - 1];
    }
}

function guardarClint(idClint) {
    var idClint = idClint;

    $.ajax({
        type: "POST",
        url: UrlGuardarClint,
        async: true,
        data: {

            idClint: idClint,
            NombreClint: document.getElementById("nombreClint").value,
            ApellidoPClint: document.getElementById("apePClint").value,
            ApellidoMClint: document.getElementById("apeMClint").value,
            FechaClint: document.getElementById("fechaClint").value,
            DireccionClint: document.getElementById("dirClint").value,
            TelefonoClint: document.getElementById("telClint").value,
            usuarioClint: document.getElementById("userClint").value,
            contraseñaClint: document.getElementById("passClint").value,
            activos: 1
        },
        success: function (data) { 
            location.href = "../cliente/VistaUsuario";
        },
        error: function (xhr, status, error) {
            alert("error");
        }
    });
}
function eliminarClint(idClint) {
    var idClint = idClint;

    $.ajax({
        type: "POST",
        url: UrlBorrarClint,
        async: true,
        data: {
            idClint: idClint,
            activos: 0
        },
        success: function (data) {
            location.href = "../home/Login";
            alert("Cuenta dada de baja con exito");
        },
        error: function (xhr, status, error) {
            alert("error");
        }
    });
}

function inicioSesion() {

    $.ajax({        
        type: "POST",
        url: Urllogin,
        async: true,
        data: {
            Username: document.getElementById("user").value,
            Password: document.getElementById("pass").value,
        },
        success: function (data)
        {
            location.href = "../cliente/VistaUsuario?idCliente=" + data;
        },
        error: function (xhr, status, error) {
            alert("Inicio de sesión incorrecta. Verifique los datos");
        }
    });
}

function editar() {
    document.getElementById('PerfilUsuario').style.display = 'none';
    document.getElementById('ModificarUsuario').style.display = 'inline';
}


function carrito() {
    var Prestamo = document.getElementById("radio1");  
    var Compra = document.getElementById("radio2");

    if (Prestamo.checked) {
        $.ajax({
            type: "POST",
            url: UrlCarrito,
            async: true,
            data: {
                Accion: "prestamo",
                Libro: document.getElementById("lib").textContent,
                Cantidad: 1
            },
            success: function (data) {
                location.href = "../cliente/Car";
            },
            error: function (xhr, status, error) {
                alert("No puede realizar dos prestamos del mismo libro por transacción");
            }
        });
    }

    else if (Compra.checked) {
        $.ajax({
            type: "POST",
            url: UrlCarrito,
            async: true,
            data: {
                Accion: "ventas",
                Libro: document.getElementById("lib").textContent,
                Cantidad: document.getElementById("cantidad").value
            },
            success: function (data) {
                location.href = "../cliente/Car";
            },
            error: function (xhr, status, error) {
                alert("error");
            }
        });
    }
}


function ActCant(idCarVent) {
    $.ajax({
        type: "POST",
        url: UrlActCant,
        async: true,
        data: {
            idCarVent: idCarVent,
            Cantidad: document.getElementById(idCarVent).value
        },
        success: function (data) {
            location.href = "../cliente/Car";
        },
        error: function (xhr, status, error) {
            alert("error");
        }
    });
}

function eliminarArt(idCarVent) {

    $.ajax({
        type: "POST",
        url: UrleliminarArt,
        async: true,
        data: {
            idCarVent: idCarVent,
        },
        success: function (data) {
            location.href = "../cliente/Car";
        },
        error: function (xhr, status, error) {
            alert("error");
        }
    });
}

function eliminarP(idCarPres) {
    $.ajax({
        type: "POST",
        url: UrleliminarP,
        async: true,
        data: {
            idCarPres: idCarPres,
        },
        success: function (data) {
            location.href = "../cliente/Car";
        },
        error: function (xhr, status, error) {
            alert("error");
        }
    });
}

function TranscFinal() {
    $.ajax({
        type: "POST",
        url: UrlTranscFinal,
        async: true,
        data: {           
        },
        success: function (data) {
            location.href = "../cliente/Car";
            alert("Transacción finalizada con exito. Dirijase a nuestra sucursal mas cercana para recojer sus productos");
        },
        error: function (xhr, status, error) {
            alert("Tiene un prestamo activo de este libro. ");
        }
    });
}

function CerrarSesion() {
    $.ajax({
        type: "POST",
        url: UrlTranscFinal,
        async: true,
        data: {
        },
        success: function (data) {
            location.href = "../cliente/Car";
        },
        error: function (xhr, status, error) {
            alert("error");
        }
    });
}