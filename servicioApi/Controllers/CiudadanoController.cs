using Azure;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;
using servicioApi.model;
using servicioApi.repository;

namespace servicioApi.data 
{

    [ApiController]
    [Route("[controller]")]
    public class CiudadanoController  : ControllerBase
    {

        private readonly ICiudadano __repository;

        public CiudadanoController(ICiudadano repository)
        {
            this.__repository = repository;
        }


        [HttpGet("/obtenerCiudadanos")]
        public List<Ciudadano> registros()
        {
            return this.__repository.listarCiudadanos();
        }


        [HttpGet("/obtenerCiudadano")]
        public ActionResult<Ciudadano> registro(string cedula)
        {
            Ciudadano registro = this.__repository.listarCiudadano(cedula);

            if(registro is null)
            {
                return BadRequest("sin_data");
            }

            return registro;
        }
    }
}