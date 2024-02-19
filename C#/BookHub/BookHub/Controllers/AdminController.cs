using Antlr.Runtime.Misc;
using BookHub.Models;
using System;
using System.Collections.Generic;
using System.Data.Entity.SqlServer;
using System.Linq;
using System.Reflection;
using System.Security.Cryptography;
using System.Text;
using System.Web;
using System.Web.Mvc;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Globalization;

namespace BookHub.Controllers
{
    public class Model
    {
        public List<DetallePrestamo> DetPre { get; set; }
        public List<DetalleVenta> DetVent { get; set; }
    }

    public class AdminController : Controller
    {
        BibliotecaContext db = new BibliotecaContext();
        EncryptMD5 pass = new EncryptMD5();

        public ActionResult Index()
        {
            return View();
        }

        #region Personal
        public ActionResult ListaPersonal()
        {
            var lista = db.Empleadoes.ToList();

            return View(lista);
        }

        public ActionResult RegistroPersonal()
        {
            var idEmp = Request.Params["idEmp"];
            if (idEmp != null)
            {
                int id = int.Parse(idEmp);
                var Empleado = db.Empleadoes.Where(x => x.idEmpleado == id).FirstOrDefault();
                ViewBag.Empleado = Empleado;
                var PassEmp = db.Empleadoes.SingleOrDefault(x => x.idEmpleado == id);
                ViewBag.PassEmp = pass.Decrypt(PassEmp.contraseñaEmp);
            }
            return View();
        }

        public JsonResult guardarEmp(int? idEmp, int idHor, string NombreEmp, string ApellidoPEmp, string ApellidoMEmp, string DireccionEmp, string TelefonoEmp, string HorarioEmp, int activos, string usuarioEmp, string constraseñaEmp)
        {
            if (idEmp != null)
            {
                var emp = db.Empleadoes.Where(x => x.idEmpleado == idEmp).FirstOrDefault();

                emp.nombresEmpleado = NombreEmp;
                emp.apellidoPEmpleado = ApellidoPEmp;
                emp.apellidoMEmpleado = ApellidoMEmp;
                emp.direccionEmpleado = DireccionEmp;
                emp.telefonoEmpleado = TelefonoEmp;
                emp.idHorario = idHor;
                emp.usuarioEmp = usuarioEmp;
                emp.contraseñaEmp = pass.Encrypt(constraseñaEmp);
                db.SaveChanges();
            }
            else
            {
                Empleado a = new Empleado();
                a.nombresEmpleado = NombreEmp;
                a.apellidoPEmpleado = ApellidoPEmp;
                a.apellidoMEmpleado = ApellidoMEmp;
                a.direccionEmpleado = DireccionEmp;
                a.telefonoEmpleado = TelefonoEmp;
                a.Activos = activos;
                a.idHorario = idHor;
                a.usuarioEmp = usuarioEmp;
                a.contraseñaEmp = pass.Encrypt(constraseñaEmp);
                db.Empleadoes.Add(a);

                db.SaveChanges();
            }
            return Json("");
        }

        public JsonResult eliminarEmp(int idEmp, int activos)
        {
            var emp = db.Empleadoes.Where(x => x.idEmpleado == idEmp).FirstOrDefault();
            emp.Activos = activos;
            db.SaveChanges();
            return Json("");
        }

        public JsonResult inicioSesionEmp(string Username, string Password)
        {
            var user = db.Empleadoes.Where(x => x.usuarioEmp == Username).FirstOrDefault();

            if (user == null)
            {
                return Json(null);
            }
            else
            {
                if (user.Activos == 1)
                {
                    if (pass.Encrypt(Password) == user.contraseñaEmp)
                    {
                        return Json(user.idEmpleado);
                    }
                    else
                    {
                        return Json(null);
                    }
                }
                else
                {
                    return Json(null);
                }
            }                     
        }

        public ActionResult AsistenciaAdmin()
        {
            var lista = db.Checadas.ToList();

            return View(lista);
        }
        #endregion;

        #region Clientes
        public ActionResult ListaClientes()
        {
            var lista = db.Clientes.ToList();
            return View(lista);
        }
        #endregion;

        #region Libreria

        #region Tipo Libros
        public ActionResult Categorias()
        {
            var lista = db.TipoLibroes.ToList();
            return View(lista);
        }

        public ActionResult MostrarImagenTL(int id)
        {
            byte[] imagenBytes = db.TipoLibroes.Find(id).imagenTL;
            return File(imagenBytes, "image/jpeg");
        }

        public ActionResult RegistroCategoria()
        {
            var idTiLi = Request.Params["idTiLi"];
            if (idTiLi != null)
            {
                int id = int.Parse(idTiLi);
                var TipoLib = db.TipoLibroes.Where(x => x.idTipoLibro == id).FirstOrDefault();
                ViewBag.TipoLibro = TipoLib;
            }
            return View();
        }

        public JsonResult guardarTiLi(int? idTiLi, string tipoLibro, string imagenTL, int activos)
        {
            if (idTiLi != null)
            {
                var tili = db.TipoLibroes.Where(x => x.idTipoLibro == idTiLi).FirstOrDefault();
                byte[] buffer;

                if (imagenTL == null)
                {
                    buffer = db.TipoLibroes.SingleOrDefault(x => x.idTipoLibro == idTiLi).imagenTL;
                    tili.tipo = tipoLibro;
                    tili.imagenTL = buffer;
                    tili.Activos = activos;
                    db.SaveChanges();

                }
                else
                {
                    buffer = Convert.FromBase64String(imagenTL);
                    tili.tipo = tipoLibro;
                    tili.imagenTL = buffer;
                    tili.Activos = activos;
                    db.SaveChanges();
                }
            }
            else
            {
                TipoLibro a = new TipoLibro();
                byte[] buffer = Convert.FromBase64String(imagenTL);
                a.tipo = tipoLibro;
                a.imagenTL = buffer;
                a.Activos = activos;
                db.TipoLibroes.Add(a);

                db.SaveChanges();
            }
            return Json("");
        }

        public JsonResult eliminarTiLi(int idTiLi, int activos)
        {
            var tili = db.TipoLibroes.Where(x => x.idTipoLibro == idTiLi).FirstOrDefault();
            tili.Activos = activos;
            db.SaveChanges();
            return Json("");
        }
        #endregion;

        #region Libros
        public ActionResult MostrarImagenLib(int id)
        {
            byte[] imagenBytes = db.Libroes.Find(id).imagenLib;
            return File(imagenBytes, "image/jpeg");
        }

        public ActionResult ListaLibros()
        {
            var lista = db.Libroes.ToList();
            return View(lista);
        }
        public ActionResult RegistroLibros()
        {
            var idLib = Request.Params["idLib"];
            var idTiLi = Request.Params["idTiLi"];
            var idEdit = Request.Params["idEdit"];
            var idPais = Request.Params["idPais"];
            
            var tipo = db.TipoLibroes.Select(x => x.tipo).ToList();
            ViewBag.tipos = tipo;
            var edit = db.Editorials.Select(x => x.nombreEditorial).ToList();
            ViewBag.edit = edit;
            var pais = db.Pais.Select(x => x.nombrePais).ToList();
            ViewBag.entF = pais;

            if (idLib != null)
            {
                int idlib = int.Parse(idLib);
                var Lib = db.Libroes.Where(x => x.idLibro == idlib).FirstOrDefault();
                ViewBag.Libro = Lib;

                int idtiLi = int.Parse(idTiLi);
                var Tili = db.TipoLibroes.Where(x => x.idTipoLibro == idtiLi).FirstOrDefault();
                ViewBag.TipoLibro = Tili;

                int idedit = int.Parse(idEdit);
                var Edit = db.Editorials.Where(x => x.idEditorial == idedit).FirstOrDefault();
                ViewBag.Editorial = Edit;

                int idpais = int.Parse(idPais);
                var Pais = db.Pais.Where(x => x.idPais == idpais).FirstOrDefault();
                ViewBag.pais = Pais;
            }
            return View();
        }


        public JsonResult guardarLib(int? idLib, string tituloLibro, string edicionLibro, string fechaLibro, string nombreAutor, string Editorial, string Pais, string Categoria, string imagenL, int? activos, int stock, float precio)
        {
            DateTime fecha = DateTime.Parse(fechaLibro);
            byte[] buffer;
            var categoria = db.TipoLibroes.SingleOrDefault(x => x.tipo == Categoria);
            var editorial = db.Editorials.SingleOrDefault(x => x.nombreEditorial == Editorial);
            var pais = db.Pais.SingleOrDefault(x => x.nombrePais == Pais);

            if (idLib != null)
            {
                var lib = db.Libroes.Where(x => x.idLibro == idLib).FirstOrDefault();
                if (imagenL == null)
                {
                    buffer = db.Libroes.SingleOrDefault(x => x.idLibro == idLib).imagenLib;
                    lib.tituloLibro = tituloLibro;
                    lib.edicionLibro = edicionLibro;
                    lib.fechaLanzamiento = fecha;
                    lib.nombreAutor = nombreAutor;
                    lib.idEditorial = editorial.idEditorial;
                    lib.idPais = pais.idPais;
                    lib.idTipoLibro = categoria.idTipoLibro;
                    lib.imagenLib = buffer;
                    lib.stock = stock;
                    lib.precio = precio;
                    db.SaveChanges();
                }
                else
                {
                    buffer = Convert.FromBase64String(imagenL);
                    lib.tituloLibro = tituloLibro;
                    lib.edicionLibro = edicionLibro;
                    lib.fechaLanzamiento = fecha;
                    lib.nombreAutor = nombreAutor;
                    lib.idEditorial = editorial.idEditorial;
                    lib.idPais = pais.idPais;
                    lib.idTipoLibro = categoria.idTipoLibro;
                    lib.imagenLib = buffer;
                    lib.stock = stock;
                    lib.precio = precio;
                    db.SaveChanges();
                }                
            }
            else
            {
                Libro a = new Libro();
                buffer = Convert.FromBase64String(imagenL);                
                a.tituloLibro = tituloLibro;
                a.edicionLibro = edicionLibro;
                a.fechaLanzamiento = fecha;
                a.nombreAutor = nombreAutor;
                a.idEditorial = editorial.idEditorial;
                a.idPais = pais.idPais;
                a.idTipoLibro = categoria.idTipoLibro;
                a.imagenLib = buffer;
                a.Activos = activos;
                db.Libroes.Add(a);

                db.SaveChanges();
            }
            return Json("");
        }
        public JsonResult eliminarLib(int idLib, int activos)
        {
            var lib = db.Libroes.Where(x => x.idLibro == idLib).FirstOrDefault();
            lib.Activos = activos;
            db.SaveChanges();
            return Json("");
        }
        #endregion;

        #region País
        public ActionResult ListaPais()
        {
            var lista = db.Pais.ToList();
            return View(lista);
        }
        public ActionResult RegistroPais()
        {
            var idPais = Request.Params["idPais"];
            if (idPais != null)
            {
                int id = int.Parse(idPais);
                var Pais = db.Pais.Where(x => x.idPais == id).FirstOrDefault();
                ViewBag.Pais = Pais;
            }
            return View();
        }
        public JsonResult guardarPais(int? idPais, string nombrePais, int activos)
        {
            if (idPais != null)
            {
                var pais = db.Pais.Where(x => x.idPais == idPais).FirstOrDefault();
                pais.nombrePais = nombrePais;
                pais.Activos = activos;
                db.SaveChanges();
            }
            else
            {
                Pai a = new Pai();
                a.nombrePais = nombrePais;
                a.Activos = activos;
                db.Pais.Add(a);
                db.SaveChanges();
            }
            return Json("");
        }
        public JsonResult eliminarPais(int idPais, int activos)
        {
            var pais = db.Pais.Where(x => x.idPais == idPais).FirstOrDefault();
            pais.Activos = activos;
            db.SaveChanges();
            return Json("");
        }
        #endregion;

        #region Editorial
        public ActionResult ListaEditorial()
        {
            var lista = db.Editorials.ToList();
            return View(lista);
        }

        public ActionResult RegistroEditorial()
        {
            var lista = db.Editorials.ToList();

            var idEdit = Request.Params["idEdit"];
            if (idEdit != null)
            {
                int id = int.Parse(idEdit);
                var Edit = db.Editorials.Where(x => x.idEditorial == id).FirstOrDefault();
                ViewBag.Editorial = Edit;
            }

            return View(lista);
        }

        public JsonResult guardarEdit(int? idEdit, string nombreEdit, int activos)
        {
            if (idEdit != null)
            {
                var edit = db.Editorials.Where(x => x.idEditorial == idEdit).FirstOrDefault();
                edit.nombreEditorial = nombreEdit;
                edit.Activos = activos;
                db.SaveChanges();
            }
            else
            {
                Editorial a = new Editorial();
                a.nombreEditorial = nombreEdit;
                a.Activos = activos;
                db.Editorials.Add(a);
                db.SaveChanges();
            }
            return Json("");
        }
        public JsonResult eliminarEdit(int idEdit, int activos)
        {
            var emp = db.Editorials.Where(x => x.idEditorial == idEdit).FirstOrDefault();
            emp.Activos = activos;
            db.SaveChanges();
            return Json("");
        }
        #endregion;

        #endregion;

        #region Prestamos

        #region ListaPrestamos

        public ActionResult ListaPrestamo()
        {
            var lista = db.DetallePrestamoes.ToList();
            return View(lista);
        }

        public ActionResult RegistroPres()
        {
            var idPres = Request.Params["idPres"];
            var idClint = Request.Params["idClint"];
            var idTPres = Request.Params["idTPres"];
            var idEmp = Request.Params["idEmp"];
            var idLib = Request.Params["idLib"];

            var cliente = db.Clientes.Select(x => x.nombresCliente).ToList();
            ViewBag.clint = cliente;
            var Tpres = db.TipoPrestamoes.Select(x => x.ModeloPrestamo).ToList();
            ViewBag.tpres = Tpres;
            var emple = db.Empleadoes.Select(x => x.nombresEmpleado).ToList();
            ViewBag.emp = emple;
            var lib = db.Libroes.Select(x => x.tituloLibro).ToList();
            ViewBag.lib = lib;

            if (idPres != null)
            {
                int idpres = int.Parse(idPres);
                var pres = db.Prestamoes.Where(x => x.idPrestamo == idpres).FirstOrDefault();
                ViewBag.Prestamo = pres;

                int idclint = int.Parse(idClint);
                var clint = db.Clientes.Where(x => x.idCliente == idclint).FirstOrDefault();
                ViewBag.Cliente = clint;

                int idtpres = int.Parse(idTPres);
                var tpres = db.TipoPrestamoes.Where(x => x.idTipoPrestamo == idtpres).FirstOrDefault();
                ViewBag.TipoPrestamo = tpres;

                int idemp = int.Parse(idEmp);
                var emp = db.Empleadoes.Where(x => x.idEmpleado == idemp).FirstOrDefault();
                ViewBag.Empleado = emp;

                int idlib = int.Parse(idLib);
                var libr = db.Libroes.Where(x => x.idLibro == idlib).FirstOrDefault();
                ViewBag.Libro = libr;
            }
            return View();
        }

        public JsonResult guardarPres(int? idPres, string fechaPres, string fechaEnt, string Cliente, string TipoPrestamo, string Empleado, string Libro, int activos)
        {
            DateTime fechapres = DateTime.Parse(fechaPres);
            DateTime fechaent = DateTime.Parse(fechaEnt);

            var cliente = db.Clientes.SingleOrDefault(x => x.nombresCliente == Cliente);
            var tipoPres = db.TipoPrestamoes.SingleOrDefault(x => x.ModeloPrestamo == TipoPrestamo);
            var empleado = db.Empleadoes.SingleOrDefault(x => x.nombresEmpleado == Empleado);
            var libro = db.Libroes.SingleOrDefault(x => x.tituloLibro == Libro);
            int? stock = (libro.stock) - 1;
            

            if (idPres != null)
            {
                var Pres = db.Prestamoes.Where(x => x.idPrestamo == idPres).FirstOrDefault();
                Pres.fechaPrestamo = fechapres;
                Pres.fechaEntrega = fechaent;
                Pres.idCliente = cliente.idCliente;
                Pres.idTipoPrestamo = tipoPres.idTipoPrestamo;
                Pres.idEmpleado = empleado.idEmpleado;

                var Dpres = db.DetallePrestamoes.Where(x => x.idPrestamo == idPres).FirstOrDefault();
                Dpres.idLibro = libro.idLibro;

                db.SaveChanges();
            }
            else
            {
                Prestamo a = new Prestamo();
                a.fechaPrestamo = fechapres;
                a.fechaEntrega = fechaent;
                a.idCliente = cliente.idCliente;
                a.idTipoPrestamo = tipoPres.idTipoPrestamo;
                a.idEmpleado = empleado.idEmpleado;
                a.Activos = activos;
                db.Prestamoes.Add(a);

                DetallePrestamo d = new DetallePrestamo();
                d.idPrestamo = a.idPrestamo;
                d.idLibro = libro.idLibro;
                d.Activos = activos;
                db.DetallePrestamoes.Add(d);

                var lib = db.Libroes.Where(x => x.tituloLibro == Libro).FirstOrDefault();
                lib.stock = stock;
                db.SaveChanges();
            }
            return Json("");
        }
        public JsonResult eliminarPres(int idPres, int activos)
        {
            var Pres = db.Prestamoes.Where(x => x.idPrestamo == idPres).FirstOrDefault();
            var detpres = db.DetallePrestamoes.Where(x => x.idPrestamo == idPres).FirstOrDefault();
            Pres.Activos = activos;
            detpres.Activos = activos;
            db.SaveChanges();
            return Json("");
        }

        #endregion;

        #region TipoPrestamos
        public ActionResult TipoPrestamo()
        {
            var lista = db.TipoPrestamoes.ToList();
            return View(lista);
        }

        public ActionResult RegistroTipoP()
        {
            var lista = db.TipoPrestamoes.ToList();

            var idTP = Request.Params["idTP"];
            if (idTP != null)
            {
                int id = int.Parse(idTP);
                var TP = db.TipoPrestamoes.Where(x => x.idTipoPrestamo == id).FirstOrDefault();
                ViewBag.TipoPrestamo = TP;
            }
            return View();
        }

        public JsonResult guardarTP(int? idTP, string modelo, int activos)
        {
            if (idTP != null)
            {
                var TP = db.TipoPrestamoes.Where(x => x.idTipoPrestamo == idTP).FirstOrDefault();
                TP.ModeloPrestamo = modelo;
                TP.Activos = activos;
                db.SaveChanges();
            }
            else
            {
                TipoPrestamo a = new TipoPrestamo();
                a.ModeloPrestamo = modelo;
                a.Activos = activos;
                db.TipoPrestamoes.Add(a);
                db.SaveChanges();
            }
            return Json("");
        }
        public JsonResult eliminarTP(int idTP, int activos)
        {
            var TP = db.TipoPrestamoes.Where(x => x.idTipoPrestamo == idTP).FirstOrDefault();
            TP.Activos = activos;
            db.SaveChanges();
            return Json("");
        }
        #endregion;
        #endregion;

        #region Ventas

        #endregion;

        #region Reportes        

        public ActionResult Reportes()
        {
            var vent = db.DetalleVentas.ToList();
            var pres = db.DetallePrestamoes.ToList();
            var fecha = Request.Params["Fecha"];
            if (fecha != null)
            {
                DateTime date = DateTime.Parse(fecha);

                string mes = date.ToString("MMMM");
                int mesnum = date.Month;
                int year = date.Year;

                
                ViewBag.Mes = mes;
                ViewBag.MesNum = mesnum;
                ViewBag.Año = year;
            }
            var modelo = new Model
            {
                DetPre = pres,
                DetVent = vent,
            };
            return View(modelo);
        }

        public JsonResult fechas(string fecha)
        {
            return Json(fecha);
        }
        #endregion;

        public ActionResult View1()
        {
            return View();
        }
    }
}
