using servicioApi.model;

namespace servicioApi.repository
{
    public interface ICiudadano
    {
        public List<Ciudadano> listarCiudadanos();

        public Ciudadano listarCiudadano(string cedula);
    }
}