using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using TFG.BD;

namespace TFG.Interfaces
{
    public partial class CRUsuarios : Form
    {
        private int _id;

        public CRUsuarios(int id)
        {
            InitializeComponent();
            _id = id;
        }

        public CRUsuarios()
        {
            InitializeComponent();
        }

        // Boton Crear
        private async void button1_Click(object sender, EventArgs e)
        {
            // Captura los datos del formulario
            string nombre = txtnombre.Text;
            string correo = txtcorreo.Text;
            string contraseña = txtcontraseña.Text;

            // Crear un objeto Users con los datos del formulario
            Users nuevoUsuario = new Users
            {
                nombre = nombre,
                correo = correo,
                contraseña = contraseña
            };

             // Ajusta la URL según sea necesario
            Administracion administracion = new Administracion();

            bool exito = await Administracion.CrearNuevoUsuario(nuevoUsuario);

            if (exito)
            {
                MessageBox.Show("Usuario creado con éxito.", "Éxito", MessageBoxButtons.OK, MessageBoxIcon.Information);
                // Opcional: limpiar los campos del formulario
                txtnombre.Clear();
                txtcorreo.Clear();
                txtcontraseña.Clear();
                this.Close();
            }
            else
            {
                MessageBox.Show("Error al crear el usuario.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        // Boton Cancelar
        private void btnCancelar_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        // Boton cerrar
        private void BtnCerrar_Click(object sender, EventArgs e)
        {
            this.Close();
        }


        // Actulizar botton
        private async void button2_Click(object sender, EventArgs e)
        {
            // Crear un objeto Users con los datos del formulario
            Users usuario = new Users
            {
                id = Convert.ToInt64(txtid.Text),  // Obtener el ID desde el campo de texto
                nombre = txtnombre.Text,           // Obtener el Nombre
                correo = txtcorreo.Text,           // Obtener el Correo
                contraseña = txtcontraseña.Text    // Obtener la Contraseña
            };

            // Instanciar la clase Administration
            Administracion admin = new Administracion();

            // Llamar al método para actualizar el usuario
            Users updatedUser = await admin.ActualizarUsuarioAsync(usuario.id, usuario);

            if (updatedUser != null)
            {
                MessageBox.Show("Usuario actualizado correctamente.");
                this.Close();
            }
            else
            {
                MessageBox.Show("Error al actualizar el usuario.");
            }
        }

        private void BarraTitulo_Paint(object sender, PaintEventArgs e)
        {

        }

        private void CRUsuarios_Load(object sender, EventArgs e)
        {

        }
    }
}
