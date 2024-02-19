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
    
    public partial class Prestamo
    {
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2214:DoNotCallOverridableMethodsInConstructors")]
        public Prestamo()
        {
            this.DetallePrestamoes = new HashSet<DetallePrestamo>();
        }
    
        public int idPrestamo { get; set; }
        public Nullable<System.DateTime> fechaPrestamo { get; set; }
        public Nullable<System.DateTime> fechaEntrega { get; set; }
        public int idCliente { get; set; }
        public int idTipoPrestamo { get; set; }
        public int idEmpleado { get; set; }
        public Nullable<int> Activos { get; set; }
    
        public virtual Cliente Cliente { get; set; }
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<DetallePrestamo> DetallePrestamoes { get; set; }
        public virtual Empleado Empleado { get; set; }
        public virtual TipoPrestamo TipoPrestamo { get; set; }
    }
}