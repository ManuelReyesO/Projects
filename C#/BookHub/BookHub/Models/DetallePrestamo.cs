//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated from a template.
//
//     Manual changes to this file may cause unexpected behavior in your application.
//     Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace BookHub.Models
{
    using System;
    using System.Collections.Generic;
    
    public partial class DetallePrestamo
    {
        public int idDetallePrestamo { get; set; }
        public int idPrestamo { get; set; }
        public int idLibro { get; set; }
        public Nullable<int> Activos { get; set; }
    
        public virtual Libro Libro { get; set; }
        public virtual Prestamo Prestamo { get; set; }
    }
}