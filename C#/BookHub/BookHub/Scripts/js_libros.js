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

function getImageBytes(file, callback) {
    var reader = new FileReader();
    reader.onload = function () {
        var bytes = new Uint8Array(reader.result);
        console.log("Bytes leídos:", bytes);
        callback(bytes);
    };
    reader.readAsArrayBuffer(file);
}

function guardarTiLi() {

    var idTiLi = getParameterByName("idTiLi");
    
    var imageFile = document.getElementById("imagenTL").files[0]

    if (imageFile === undefined) {
        $.ajax({
            type: "POST",
            url: UrlGuardarTiLi,
            async: true,
            data: {
                idTiLi: idTiLi,
                tipoLibro: document.getElementById("tipoLibro").value,
                activos: 1
            },
            success: function (data) {
                location.href = "../admin/Categorias";
            },
            error: function (xhr, status, error) {
                alert("error");
            }
        });
    } else {
        getImageBytes(imageFile, function (bytes) {
            var base64String = btoa(String.fromCharCode.apply(null, bytes));
            $.ajax({
                type: "POST",
                url: UrlGuardarTiLi,
                async: true,
                data: {
                    idTiLi: idTiLi,
                    tipoLibro: document.getElementById("tipoLibro").value,
                    imagenTL: base64String,
                    activos: 1
                },
                success: function (data) {
                    location.href = "../admin/Categorias";
                },
                error: function (xhr, status, error) {
                    alert("error");
                }
            });
        });
    }
    
}

function eliminarTiLi(idTiLi) {
    var idTiLi = idTiLi;

    $.ajax({
        type: "POST",
        url: UrlBorrarTiLi,
        async: true,
        data: {
            idTiLi: idTiLi,
            activos: 0
        },
        success: function (data) {
            location.href = "../admin/Categorias";
        },
        error: function (xhr, status, error) {
            alert("error");
        }
    });
}


function guardarPais() {

    var idPais = getParameterByName("idPais");

    $.ajax({
        type: "POST",
        url: UrlGuardarPais,
        async: true,
        data: {
            idPais: idPais,
            nombrePais: document.getElementById("Pais").value,
            activos: 1
        },
        success: function (data) {
            location.href = "../admin/ListaPais";
        },
        error: function (xhr, status, error) {
            alert("error");
        }
    });
}

function eliminarPais(idPais) {
    var idPais = idPais;

    $.ajax({
        type: "POST",
        url: UrlBorrarPais,
        async: true,
        data: {
            idPais: idPais,
            activos: 0
        },
        success: function (data) {
            location.href = "../admin/ListaPais";
        },
        error: function (xhr, status, error) {
            alert("error");
        }
    });
}

function guardarEdit() {

    var idEdit = getParameterByName("idEdit");
    $.ajax({
        type: "POST",
        url: UrlGuardarEdit,
        async: true,
        data: {
            idEdit: idEdit,
            nombreEdit: document.getElementById("Edit").value,
            activos: 1
        },
        success: function (data) {
            location.href = "../admin/ListaEditorial";
        },
        error: function (xhr, status, error) {
            alert("error");
        }
    });
}

function eliminarEdit(idEdit) {
    var idEdit = idEdit;

    $.ajax({
        type: "POST",
        url: UrlBorrarPais,
        async: true,
        data: {
            idEdit: idEdit,
            activos: 0
        },
        success: function (data) {
            location.href = "../admin/ListaEditorial";
        },
        error: function (xhr, status, error) {
            alert("error");
        }
    });
}


function guardarLib() {

    var idLib = getParameterByName("idLib");
    var imageFile = document.getElementById("portada").files[0]

    if (imageFile === undefined) {
        $.ajax({
            type: "POST",
            url: UrlGuardarLib,
            async: true,
            data: {
                idLib: idLib,
                tituloLibro: document.getElementById("titulo").value,
                edicionLibro: document.getElementById("edicion").value,
                fechaLibro: document.getElementById("fechaL").value,
                nombreAutor: document.getElementById("autor").value,
                Editorial: document.getElementById("editorial").value,
                Pais: document.getElementById("pais").value,
                Categoria: document.getElementById("categoria").value,
                stock: document.getElementById("stock").value,
                precio: document.getElementById("precio").value,
                activos: 1
            },
            success: function (data) {
                location.href = "../admin/ListaLibros";
            },
            error: function (xhr, status, error) {
                alert("error");
            }
        });
    }
    else {
        getImageBytes(imageFile, function (bytes) {
            var base64String = btoa(String.fromCharCode.apply(null, bytes));
            $.ajax({
                type: "POST",
                url: UrlGuardarLib,
                async: true,
                data: {
                    idLib: idLib,
                    tituloLibro: document.getElementById("titulo").value,
                    edicionLibro: document.getElementById("edicion").value,
                    fechaLibro: document.getElementById("fechaL").value,
                    nombreAutor: document.getElementById("autor").value,
                    Editorial: document.getElementById("editorial").value,
                    Pais: document.getElementById("pais").value,
                    Categoria: document.getElementById("categoria").value,
                    stock: document.getElementById("stock").value,
                    precio: document.getElementById("precio").value,
                    imagenL: base64String,
                    activos: 1
                },
                success: function (data) {
                    location.href = "../admin/ListaLibros";
                },
                error: function (xhr, status, error) {
                    alert("error");
                }
            });
        });
    }     
}

function eliminarLib(idLib) {
    var idLib = idLib;

    $.ajax({
        type: "POST",
        url: UrlBorrarLib,
        async: true,
        data: {
            idLib: idLib,
            activos: 0
        },
        success: function (data) {
            location.href = "../admin/ListaLibros";
        },
        error: function (xhr, status, error) {
            alert("error");
        }
    });
}


function guardarTP() {

    var idTP = getParameterByName("idTP");
    $.ajax({
        type: "POST",
        url: UrlGuardarTP,
        async: true,
        data: {
            idTP: idTP,
            modelo: document.getElementById("tipoPre").value,
            activos: 1
        },
        success: function (data) {
            location.href = "../admin/TipoPrestamo";
        },
        error: function (xhr, status, error) {
            alert("error");
        }
    });
}

function eliminarTP(idTP) {
    var idTP = idTP;

    $.ajax({
        type: "POST",
        url: UrlBorrarTP,
        async: true,
        data: {
            idTP: idTP,
            activos: 0
        },
        success: function (data) {
            location.href = "../admin/TipoPrestamo";
        },
        error: function (xhr, status, error) {
            alert("error");
        }
    });
}


function guardarPres() {
    var idPres = getParameterByName("idPres");
    $.ajax({
        type: "POST",
        url: UrlGuardarPres,
        async: true,
        data: {
            idPres: idPres,
            fechaPres: document.getElementById("fechaPres").value,
            fechaEnt: document.getElementById("fechaEnt").value,
            Cliente: document.getElementById("cliente").value,
            TipoPrestamo: document.getElementById("tpres").value,
            Empleado: document.getElementById("emp").value,
            Libro: document.getElementById("lib").value,
            activos: 1
        },
        success: function (data) {
            location.href = "../admin/ListaPrestamo";
        },
        error: function (xhr, status, error) {
            alert("error");
        }
    });
}

function eliminarPres(idPres) {
    var idPres = idPres;

    $.ajax({
        type: "POST",
        url: UrlBorrarPres,
        async: true,
        data: {
            idPres: idPres,
            activos: 0
        },
        success: function (data) {
            location.href = "../admin/ListaPrestamo";
        },
        error: function (xhr, status, error) {
            alert("error");
        }
    });
}