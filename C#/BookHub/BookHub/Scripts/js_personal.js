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

function guardarEmp() {

    var idEmp = getParameterByName("idEmp");
    var idHor;
    var horEmp = document.getElementById("horEmp").value;

    if (horEmp === 'Matutino') {
        idHor = 1;
    }
    else if (horEmp === 'Vespertino'){
        idHor = 2;
    }

    $.ajax({
        type: "POST",
        url: UrlGuardarEmp,
        async: true,
        data: {

            idEmp: idEmp,
            NombreEmp: document.getElementById("nombre").value,
            ApellidoPEmp: document.getElementById("apePEmp").value,
            ApellidoMEmp: document.getElementById("apeMEmp").value,
            DireccionEmp: document.getElementById("dirEmp").value,
            TelefonoEmp: document.getElementById("telEmp").value,
            usuarioEmp: document.getElementById("userEmp").value,
            constraseñaEmp: document.getElementById("contEmp").value,
            idHor: idHor,
            activos: 1
        },
        success: function (data) {
            location.href = "../admin/ListaPersonal";
        },
        error: function (xhr, status, error) {
            alert("error");
        }
    });
}

function eliminarEmp(idemp) {
    var idEmp = idemp;

    $.ajax({
        type: "POST",
        url: UrlBorrarEmp,
        async: true,
        data: {
            idEmp: idEmp,
            activos: 0
        },
        success: function (data) {
            location.href = "../admin/ListaPersonal";
        },
        error: function (xhr, status, error) {
            alert("error");
        }
    });
}

function inicioSesionEmp() {

    $.ajax({
        type: "POST",
        url: UrlloginEmp,
        async: true,
        data: {
            Username: document.getElementById("user").value,
            Password: document.getElementById("pass").value,
        },
        success: function (data) {
            location.href = "../admin/index?idEmp="; + data
        },
        error: function (xhr, status, error) {
            alert("Inicio de sesión incorrecta. Verifique los datos");
        }
    });
}

function checkE() {

    $.ajax({
        type: "POST",
        url: UrlcheckE,
        async: true,
        data: {
            idEmpleado: document.getElementById("emp").value,
        },
        success: function (data) {
            if (data === "idError") {
                alert("Inserte un numero de empleado valido");
            }
            else if (data === "AsistError") {
                alert("Solo una Entrada/Salida por dia");
            }
            else {
                location.href = "../home/respuesta?idEmp=" + data;
            }
        },
        error: function (xhr, status, error) {
            alert("Inserte un numero de empleado valido");
        }
    });
}

function checkS() {

    $.ajax({
        type: "POST",
        url: UrlcheckS,
        async: true,
        data: {
            idEmpleado: document.getElementById("emp").value,
        },
        success: function (data) {
            if (data === "idError") {
                alert("Inserte un numero de empleado valido");
            }
            else if (data === "AsistError") {
                alert("Solo una Entrada/Salida por dia");
            }
            else {
                location.href = "../home/respuesta?idEmp=" + data;
            }            
        },
        error: function (xhr, status, error) {
            alert("Inserte un numero de empleado valido");
        }
    });
}

function fechas() {

    $.ajax({
        type: "POST",
        url: Urlfechas,
        async: true,
        data: {
            Fecha: document.getElementById("año").value,
        },
        success: function (data) {
            location.href = "../admin/Reportes?Fecha=" + data;
        },
        error: function (xhr, status, error) {
            alert("Inserte una fecha valida");
        }
    });
}