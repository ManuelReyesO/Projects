using Antlr.Runtime.Misc;
using BookHub.Models;
using System;
using System.CodeDom;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using System.Web;
using System.Web.Http;
using System.Web.Mvc;

namespace BookHub.Controllers
{
    public class CarritoCompras
    {
        public List<carritoPrestamo> carritoPrestamos { get; set; }
        public List<carritoVenta> carritoVentas { get; set; }
    }

    public class ClienteController : Controller
    {

        static int? idClient;

        BibliotecaContext db = new BibliotecaContext();
        EncryptMD5 pass = new EncryptMD5();

        public ActionResult Index()
        {
            var Cliente = db.Clientes.Where(x => x.idCliente == idClient).FirstOrDefault();
            ViewBag.Cliente = Cliente;
            var carritoP = db.carritoPrestamoes.Select(x => x.idCliente).Count();
            var carritoV = db.carritoVentas.Select(x => x.idCarritoVenta).Count();
            ViewBag.Items = carritoP + carritoV;
            return View();
        }

        public ActionResult navbar()
        {
            var Cliente = db.Clientes.Where(x => x.idCliente == idClient).FirstOrDefault();
            ViewBag.Cliente = Cliente;
            var carritoP = db.carritoPrestamoes.Select(x => x.idCliente).Count();
            var carritoV = db.carritoVentas.Select(x => x.idCarritoVenta).Count();
            ViewBag.Items = carritoP + carritoV;
            return PartialView("_navbar");
        }

        public ActionResult Categories()
        {
            var Cliente = db.Clientes.Where(x => x.idCliente == idClient).FirstOrDefault();
            ViewBag.Cliente = Cliente;
            var carritoP = db.carritoPrestamoes.Select(x => x.idCliente).Count();
            var carritoV = db.carritoVentas.Select(x => x.idCarritoVenta).Count();
            ViewBag.Items = carritoP + carritoV;
            var lista = db.TipoLibroes.ToList();
            return View(lista);
        }

        #region Categorias
        public ActionResult Novela()
        {
            var lista = db.Libroes.ToList();
            return View(lista);
        }
        public ActionResult Cientifico()
        {
            var lista = db.Libroes.ToList();
            return View(lista);
        }
        public ActionResult Epopeya()
        {
            var lista = db.Libroes.ToList();
            return View(lista);
        }
        public ActionResult CienciaFiccion()
        {
            var lista = db.Libroes.ToList();
            return View(lista);
        }
        public ActionResult Terror()
        {
            var lista = db.Libroes.ToList();
            return View(lista);
        }
        public ActionResult Mangas()
        {
            var lista = db.Libroes.ToList();
            return View(lista);
        }
        public ActionResult Comics()
        {
            var lista = db.Libroes.ToList();
            return View(lista);
        }
        #endregion;

        public ActionResult Books()
        {
            var Cliente = db.Clientes.Where(x => x.idCliente == idClient).FirstOrDefault();
            ViewBag.Cliente = Cliente;
            var carritoP = db.carritoPrestamoes.Select(x => x.idCliente).Count();
            var carritoV = db.carritoVentas.Select(x => x.idCarritoVenta).Count();
            ViewBag.Items = carritoP + carritoV;
            var lista = db.Libroes.ToList();
            return View(lista);
        }

        #region Ventas
        public ActionResult Sold()
        {
            var Cliente = db.Clientes.Where(x => x.idCliente == idClient).FirstOrDefault();
            ViewBag.Cliente = Cliente;
            var carritoP = db.carritoPrestamoes.Select(x => x.idCliente).Count();
            var carritoV = db.carritoVentas.Select(x => x.idCarritoVenta).Count();
            ViewBag.Items = carritoP + carritoV;

            var idLib = Request.Params["idLib"];
            var idTiLi = Request.Params["idTiLi"];
            var idEdit = Request.Params["idEdit"];
            var idPais = Request.Params["idPais"];

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
            ViewBag.Pais = Pais;

            return View();
        }

        public JsonResult carrito(string Accion, string Libro, int Cantidad)
        {            
            string fechaI = DateTime.Now.ToString("yyyy-MM-dd");
            string fechaE = DateTime.Now.AddDays(21).ToString("yyyy-MM-dd");
            DateTime date = DateTime.Parse(fechaI);
            DateTime dateE = DateTime.Parse(fechaE);
            var libro = db.Libroes.SingleOrDefault(x => x.tituloLibro == Libro);
            var ver = db.carritoPrestamoes.SingleOrDefault(x => x.idLibro == libro.idLibro);

            if (Accion == "prestamo")
            {
                if (ver == null)
                {
                    carritoPrestamo a = new carritoPrestamo();
                    a.fechaPrestamo = date;
                    a.fechaEntrega = dateE;
                    a.idCliente = Convert.ToInt16(idClient);
                    a.idTipoPrestamo = 1;
                    a.idEmpleado = 29;
                    a.cantidad = Cantidad;
                    a.idLibro = libro.idLibro;
                    db.carritoPrestamoes.Add(a);
                }
                else
                {
                    return Json(null);
                }
            }
            else if(Accion == "ventas")
            {
                carritoVenta a = new carritoVenta();
                a.fechaVenta = date;
                a.idCliente = Convert.ToInt16(idClient);
                a.idEmpleado = 29;
                a.cantidad = Cantidad;
                a.idLibro = libro.idLibro;
                db.carritoVentas.Add(a);
            }

            db.SaveChanges();
            return Json("");
        }

        public ActionResult Car()
        {
            var Cliente = db.Clientes.Where(x => x.idCliente == idClient).FirstOrDefault();
            ViewBag.Cliente = Cliente;
            var carritoP = db.carritoPrestamoes.Select(x => x.idCliente).Count();
            var carritoV = db.carritoVentas.Select(x => x.idCarritoVenta).Count();
            ViewBag.Items = carritoP + carritoV;

            var lista = db.carritoPrestamoes.ToList();
            var lista2 = db.carritoVentas.ToList();
            var articulosP = db.carritoPrestamoes.Where(x => x.idCliente == idClient);
            var articulosV = db.carritoVentas.Where(x => x.idCliente == idClient);

            int precio = 0;

            ViewBag.ArtP = articulosP.Sum(x => x.cantidad);
            ViewBag.ArtV = articulosV.Sum(x => x.cantidad);
            ViewBag.Precio = precio;
            ViewBag.idCliente = idClient;
            var modelo = new CarritoCompras
            {
                carritoPrestamos = lista,
                carritoVentas = lista2,
            };
            return View(modelo);
        }

        public JsonResult ActCant(int idCarVent, int Cantidad)
        {
            var Vent = db.carritoVentas.Where(x => x.idCarritoVenta == idCarVent).FirstOrDefault();
            Vent.cantidad = Cantidad;
            db.SaveChanges();
            return Json("");
        }
        public JsonResult eliminarArt(int? idCarVent, int? idCarPres)
        {
            var Vent = db.carritoVentas.Find(idCarVent);
            db.carritoVentas.Remove(Vent);
            db.SaveChanges();
            return Json("");
        }

        public JsonResult eliminarP(int? idCarPres)
        {
            var Pres = db.carritoPrestamoes.Find(idCarPres);
            db.carritoPrestamoes.Remove(Pres);
            db.SaveChanges();
            return Json("");
        }
        public JsonResult TranscFinal()
        {            
            using (var transaction = db.Database.BeginTransaction())
            {
                try
                {
                    foreach (var item in db.carritoPrestamoes)
                    {
                        var ver = db.DetallePrestamoes.Where(x => x.idLibro == item.idLibro).OrderByDescending(x => x.idDetallePrestamo).FirstOrDefault();

                        if (ver == null || ver.Activos == 0)
                        {
                            Prestamo a = new Prestamo();
                            a.fechaPrestamo = item.fechaPrestamo;
                            a.fechaEntrega = item.fechaEntrega;
                            a.idCliente = item.idCliente;
                            a.idTipoPrestamo = item.idTipoPrestamo;
                            a.idEmpleado = item.idEmpleado;
                            a.Activos = 1;
                            db.Prestamoes.Add(a);
                            db.SaveChanges();

                            DetallePrestamo d = new DetallePrestamo();
                            d.idPrestamo = a.idPrestamo;
                            d.idLibro = Convert.ToInt16(item.idLibro);
                            d.Activos = 1;
                            db.DetallePrestamoes.Add(d);

                            var lib = db.Libroes.Where(x => x.idLibro == item.idLibro).FirstOrDefault();
                            lib.stock = lib.stock - item.cantidad;
                        }
                        else
                        {
                            return Json(null);
                        }
                    }

                    foreach (var item in db.carritoVentas)
                    {
                        Venta v = new Venta();
                        v.idCliente = item.idCliente;
                        v.idEmpleado = item.idEmpleado;
                        v.fechaVenta = item.fechaVenta;
                        db.Ventas.Add(v);
                        db.SaveChanges();

                        DetalleVenta vd = new DetalleVenta();
                        vd.idVenta = v.idVenta;
                        vd.idLibro = Convert.ToInt16(item.idLibro);
                        db.DetalleVentas.Add(vd);

                        var lib = db.Libroes.Where(x => x.idLibro == item.idLibro).FirstOrDefault();
                        lib.stock = lib.stock - item.cantidad;
                    }

                    db.carritoPrestamoes.RemoveRange(db.carritoPrestamoes);
                    db.carritoVentas.RemoveRange(db.carritoVentas);
                    db.SaveChanges();

                    transaction.Commit();
                }
                catch (Exception ex)
                {
                    transaction.Rollback();
                    throw ex;
                }
            }
            return Json("");
        }

        #endregion;


        #region Cliente
        public ActionResult VistaUsuario()
        {
            var Clien = db.Clientes.Where(x => x.idCliente == idClient).FirstOrDefault();
            ViewBag.Cliente = Clien;
            var carritoP = db.carritoPrestamoes.Select(x => x.idCliente).Count();
            var carritoV = db.carritoVentas.Select(x => x.idCarritoVenta).Count();
            ViewBag.Items = carritoP + carritoV;

            if (Request.Params["idCliente"] != null)
            {
                idClient = int.Parse(Request.Params["idCliente"]);
                var Cliente = db.Clientes.Where(x => x.idCliente == idClient).FirstOrDefault();
                ViewBag.Cliente = Cliente;
                var passClint = db.Clientes.SingleOrDefault(x => x.idCliente == idClient);
                ViewBag.passClint = pass.Decrypt(passClint.contraseñaCliente);
            }

            ViewBag.Accion = 1;
            return View();
        }

        public JsonResult guardarClint(int? idClint, string NombreClint, string ApellidoPClint, string ApellidoMClint, string DireccionClint, string TelefonoClint, string FechaClint, string usuarioClint, string contraseñaClint, int activos)
        {
            DateTime fecha = DateTime.Parse(FechaClint);
            if (idClint != null)
            {
                var clint = db.Clientes.Where(x => x.idCliente == idClint).FirstOrDefault();

                clint.nombresCliente = NombreClint;
                clint.apellidoPCliente = ApellidoPClint;
                clint.apellidoMCliente = ApellidoMClint;
                clint.fechaNacimientoCliente = fecha;
                clint.direccionCliente = DireccionClint;
                clint.telefonoCliente = TelefonoClint;
                clint.usuarioCliente = usuarioClint;
                clint.contraseñaCliente = pass.Encrypt(contraseñaClint);
                db.SaveChanges();
                return Json(idClint);
            }
            else
            {
                var user = db.Clientes.Where(x => x.usuarioCliente == usuarioClint).FirstOrDefault();

                if (user == null)
                {
                    Cliente a = new Cliente();
                    a.nombresCliente = NombreClint;
                    a.apellidoPCliente = ApellidoPClint;
                    a.apellidoMCliente = ApellidoMClint;
                    a.fechaNacimientoCliente = fecha;
                    a.direccionCliente = DireccionClint;
                    a.telefonoCliente = TelefonoClint;
                    a.usuarioCliente = usuarioClint;
                    a.contraseñaCliente = pass.Encrypt(contraseñaClint);
                    a.Activos = activos;
                    db.Clientes.Add(a);
                    db.SaveChanges();

                    return Json(a.idCliente);
                }
                else
                {
                    return Json(null);
                }
            }
        }
        public JsonResult eliminarClint(int idClint, int activos)
        {
            var clint = db.Clientes.Where(x => x.idCliente == idClint).FirstOrDefault();
            clint.Activos = activos;
            db.SaveChanges();
            return Json("");
        }
       
        #endregion;
    }
}
