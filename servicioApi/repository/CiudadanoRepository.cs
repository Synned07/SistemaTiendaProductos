
using Microsoft.Data.SqlClient;
using Microsoft.EntityFrameworkCore;
using servicioApi.data;
using servicioApi.model;

namespace servicioApi.repository 
{
    public class CiudadanoRepository : ICiudadano
    {

        private readonly CiudadanoDBContext __context;

        public CiudadanoRepository(CiudadanoDBContext context)
        {
            this.__context = context;
        }

        public Ciudadano listarCiudadano(string cedula)
        {
            Ciudadano registro = null;

            using(SqlConnection connection = new SqlConnection(this.__context.Database.GetConnectionString()))
            {
                connection.Open();

                using(SqlCommand command = new SqlCommand("PCR_listarCiudadanos", connection))
                {
                    command.CommandType = System.Data.CommandType.StoredProcedure;

                    command.Parameters.Add(new SqlParameter("@Cedula", cedula));

                    using(SqlDataReader reader = command.ExecuteReader())
                    {
                        while(reader.Read())
                        {
                            registro = new Ciudadano(){
                                ciudadano_id = (int) reader["ciudadano_id"],
                                ciudadano_nombre = (string) reader["ciudadano_nombre"],
                                ciudadano_apellido = (string) reader["ciudadano_apellido"],
                                ciudadano_cedula = (string) reader["ciudadano_cedula"],
                                ciudadano_celular = (string) reader["ciudadano_celular"],
                                ciudadano_email = (string) reader["ciudadano_email"],
                                ciudadano_fechaNacimiento = (string) reader["ciudadano_fechaNacimiento"],
                                ciudadano_estado = (bool) reader["ciudadano_estado"]
                            };
                        }

                        connection.Close();
                    }
                }
            }
            return registro;
        }

        public List<Ciudadano> listarCiudadanos()
        {
            List<Ciudadano> ciudadanos = new List<Ciudadano>();

        
            using(SqlConnection connection = new SqlConnection(this.__context.Database.GetConnectionString()))
            {
                connection.Open();

                using(SqlCommand command = new SqlCommand("PCR_listarCiudadanos", connection))
                {
                    command.CommandType = System.Data.CommandType.StoredProcedure;

                    using(SqlDataReader reader = command.ExecuteReader())
                    {
                        while(reader.Read())
                        {
                            Console.WriteLine(reader["ciudadano_id"]);

                            Ciudadano registro = new Ciudadano(){
                                ciudadano_id = (int) reader["ciudadano_id"],
                                ciudadano_nombre = (string) reader["ciudadano_nombre"],
                                ciudadano_apellido = (string) reader["ciudadano_apellido"],
                                ciudadano_cedula = (string) reader["ciudadano_cedula"],
                                ciudadano_celular = (string) reader["ciudadano_celular"],
                                ciudadano_email = (string) reader["ciudadano_email"],
                                ciudadano_fechaNacimiento = (string) reader["ciudadano_fechaNacimiento"],
                                ciudadano_estado = (bool) reader["ciudadano_estado"]
                            };

                            ciudadanos.Add(registro);
                        }

                        connection.Close();
                    }
                }
            }
            return ciudadanos;
        }

    }
}