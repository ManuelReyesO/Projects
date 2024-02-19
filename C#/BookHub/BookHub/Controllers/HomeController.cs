using BookHub.Models;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Security.Cryptography;
using System.Security.Policy;
using System.Text;
using System.Web;
using System.Web.Mvc;
using System.Web.WebPages;

namespace BookHub.Controllers
{
    public class EncryptMD5
    {
        public string Encrypt(string cadena)
        {
            string hash = "BookHub";
            byte[]data=UTF8Encoding.UTF8.GetBytes(cadena);

            MD5 md5 = MD5.Create();
            TripleDES tripledes = TripleDES.Create();

            tripledes.Key = md5.ComputeHash(UTF8Encoding.UTF8.GetBytes(hash));
            tripledes.Mode = CipherMode.ECB;

            ICryptoTransform transform = tripledes.CreateEncryptor();
            byte[] result = transform.TransformFinalBlock(data, 0, data.Length);

            return Convert.ToBase64String(result);
        }

        public string Decrypt(string cadena)
        {
            string hash = "BookHub";
            byte[] data = Convert.FromBase64String(cadena);

            MD5 md5 = MD5.Create();
            TripleDES tripledes = TripleDES.Create();

            tripledes.Key = md5.ComputeHash(UTF8Encoding.UTF8.GetBytes(hash));
            tripledes.Mode = CipherMode.ECB;

            ICryptoTransform transform = tripledes.CreateDecryptor();
            byte[] result = transform.TransformFinalBlock(data, 0, data.Length);

            return UTF8Encoding.UTF8.GetString(result);
        }
    }   

    public class HomeController : Controller
    {
        BibliotecaContext db = new BibliotecaContext();
        EncryptMD5 pass = new EncryptMD5();

        public ActionResult Index()
        {
            return View();
        }

        public ActionResult Login()
        {
            return View();
        }    

        public ActionResult AdminLog()
        {
            return View();
        }

        public ActionResult Categories()
        {
            var lista = db.TipoLibroes.ToList();
            return View(lista);
        }

        public ActionResult Books()
        {
            var lista = db.Libroes.ToList();
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
        #region Clientes

        public ActionResult RegistroClientes()
        {
            var idClint = Request.Params["idClint"];
            if (idClint != null)
            {                
                int id = int.Parse(idClint);
                var Cliente = db.Clientes.Where(x => x.idCliente == id).FirstOrDefault();
                var passClint = db.Clientes.SingleOrDefault(x => x.idCliente == id);
                ViewBag.Cliente = Cliente;
                ViewBag.passClint = pass.Decrypt(passClint.contraseñaCliente);
                return View();
            }
            return View();
        }
        public ActionResult VistaUsuario()
        {
            var idCliente = Request.Params["idCliente"];
            if (idCliente != null)
            {
                int id = int.Parse(idCliente);
                var Cliente = db.Clientes.Where(x => x.idCliente == id).FirstOrDefault();
                ViewBag.Cliente = Cliente;
            }
            return View();
        }

        public JsonResult guardarClint(int? idClint, string NombreClint, string ApellidoPClint, string ApellidoMClint, string DireccionClint, string TelefonoClint, string FechaClint, string usuarioClint, string contraseñaClint,  int activos)
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

        public JsonResult inicioSesion(string Username, string Password)
        {
            var clint = db.Clientes.Where(x => x.usuarioCliente == Username).FirstOrDefault();
            if (clint != null)
            {
                if (clint.Activos == 1)
                {
                    if (Password == pass.Decrypt(clint.contraseñaCliente))
                    {
                        return Json(clint.idCliente);
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
            else
            {
                return Json(null);
            }
                      
        }
        #endregion;

        public ActionResult Sold()
        {
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

        public ActionResult Checadas() 
        {
            var emps = db.Empleadoes.Select(x => x.nombresEmpleado);
            ViewBag.Emps = emps;
            return View(); 
        }
        public ActionResult respuesta()
        {
            var dateES = DateTime.Now;
            var idEmp = Request.Params["idEmp"];
                       
            ViewBag.Asistencia = dateES;

            int idemp = int.Parse(idEmp);
            var emp = db.Empleadoes.Where(x => x.idEmpleado == idemp).FirstOrDefault();
            ViewBag.Empleado = emp;

            return View();
        }

        public JsonResult checkE(int idEmpleado)
        {            
            var dateES = DateTime.Now.ToString("yyyy-MM-dd");
            var date = DateTime.Now.ToString();
            DateTime ES = DateTime.Parse(dateES);
            TimeSpan Ent = TimeSpan.Parse(DateTime.Now.ToString("HH:mm:ss"));
            var id = db.Empleadoes.Where(x => x.idEmpleado == idEmpleado).FirstOrDefault();

            var asist = db.Checadas.Where(x => x.idEmpleado == idEmpleado && x.fecha_es == ES).FirstOrDefault();
            if(asist == null)
            {
                if (id != null)
                {
                    Checada ch = new Checada();
                    ch.entradaChecada = Ent;
                    ch.idEmpleado = idEmpleado;
                    ch.fecha_es = ES;
                    db.Checadas.Add(ch);
                    db.SaveChanges();
                    return Json(idEmpleado);
                }
                return Json("idError");
            }            
            return Json("AsistError");    
        }

        public JsonResult checkS(int idEmpleado)
        {
            var dateES = DateTime.Now.ToString("yyyy-MM-dd");
            var date = DateTime.Now.ToString();
            DateTime ES = DateTime.Parse(dateES);
            TimeSpan Ent = TimeSpan.Parse(DateTime.Now.ToString("HH:mm:ss"));
            var id = db.Empleadoes.Where(x => x.idEmpleado == idEmpleado).FirstOrDefault();
            var asist = db.Checadas.Where(x => x.idEmpleado == idEmpleado && x.fecha_es == ES).FirstOrDefault();

            if (asist.salidaChecada == null)
            {
                if (id != null)
                {
                    asist.salidaChecada = Ent;
                    db.SaveChanges();
                    return Json(idEmpleado);
                }
                return Json("idError");
            }
            return Json("AsistError");
        }
    }
}