using Microsoft.EntityFrameworkCore;
using servicioApi.model;

namespace servicioApi.data 
{
    public class CiudadanoDBContext : DbContext 
    {
        public CiudadanoDBContext(DbContextOptions<CiudadanoDBContext> options) : base(options) { }

        public DbSet<Ciudadano> Ciudadano { set; get; }
    }
}