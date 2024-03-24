using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace servicioApi.Migrations
{
    /// <inheritdoc />
    public partial class migracion : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "Ciudadano",
                columns: table => new
                {
                    ciudadano_id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    ciudadano_nombre = table.Column<string>(type: "nvarchar(max)", nullable: true),
                    ciudadano_apellido = table.Column<string>(type: "nvarchar(max)", nullable: true),
                    ciudadano_cedula = table.Column<string>(type: "nvarchar(max)", nullable: true),
                    ciudadano_celular = table.Column<string>(type: "nvarchar(max)", nullable: true),
                    ciudadano_email = table.Column<string>(type: "nvarchar(max)", nullable: true),
                    ciudadano_fechaNacimiento = table.Column<string>(type: "nvarchar(max)", nullable: true),
                    ciudadano_estado = table.Column<int>(type: "int", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Ciudadano", x => x.ciudadano_id);
                });
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "Ciudadano");
        }
    }
}
