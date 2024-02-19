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
    
    public partial class Libro
    {
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2214:DoNotCallOverridableMethodsInConstructors")]
        public Libro()
        {
            this.carritoPrestamoes = new HashSet<carritoPrestamo>();
            this.carritoVentas = new HashSet<carritoVenta>();
            this.DetallePrestamoes = new HashSet<DetallePrestamo>();
            this.DetalleVentas = new HashSet<DetalleVenta>();
        }
    
        public int idLibro { get; set; }
        public string tituloLibro { get; set; }
        public string edicionLibro { get; set; }
        public Nullable<System.DateTime> fechaLanzamiento { get; set; }
        public string nombreAutor { get; set; }
        public int idEditorial { get; set; }
        public int idPais { get; set; }
        public int idTipoLibro { get; set; }
        public byte[] imagenLib { get; set; }
        public Nullable<int> Activos { get; set; }
        public Nullable<int> stock { get; set; }
        public Nullable<double> precio { get; set; }
    
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<carritoPrestamo> carritoPrestamoes { get; set; }
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<carritoVenta> carritoVentas { get; set; }
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<DetallePrestamo> DetallePrestamoes { get; set; }
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<DetalleVenta> DetalleVentas { get; set; }
        public virtual Editorial Editorial { get; set; }
        public virtual Pai Pai { get; set; }
        public virtual TipoLibro TipoLibro { get; set; }
    }
}