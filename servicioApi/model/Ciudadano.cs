using System.ComponentModel.DataAnnotations;

namespace servicioApi.model 
{
    public class Ciudadano 
    {
        [Key]
        public int ciudadano_id { set; get; }
    
        public string? ciudadano_nombre { set; get; }

        public string? ciudadano_apellido { set; get; }

        public string? ciudadano_cedula { set; get; }

        public string? ciudadano_celular { set; get; }

        public string? ciudadano_email { set; get; }

        public string? ciudadano_fechaNacimiento { set; get; }

        public bool? ciudadano_estado { set; get; }
    }
}