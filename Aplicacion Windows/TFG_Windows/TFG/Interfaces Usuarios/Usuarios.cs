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
    public partial class Usuarios : Form
    {
        public Usuarios()
        {
            InitializeComponent();
        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }

        private async void Usuarios_Load(object sender, EventArgs e)
        {
            WebConsumer consumer = new WebConsumer("http://localhost:8089"); // Ajusta la URL según corresponda
            List<Users> usuarios = await consumer.ObtenerUsuariosAsync("/users"); // Ajusta el endpoint según tu API

            if (usuarios != null && usuarios.Count > 0)
            {
                dataGridView1.DataSource = usuarios;

                // Opcionalmente puedes configurar las columnas de DataGridView si deseas personalizar los encabezados
                dataGridView1.Columns["id"].HeaderText = "ID Usuario";
                dataGridView1.Columns["nombre"].HeaderText = "Nombre";
                dataGridView1.Columns["contraseña"].HeaderText = "Contraseña";
                dataGridView1.Columns["correo"].HeaderText = "Correo";
                 // Ocultar si es sensible

                // Agrega el evento de formato de celda para enmascarar la columna de contraseña
                dataGridView1.CellFormatting += dataGridView1_CellFormatting;
            }
            else
            {
                MessageBox.Show("No se encontraron usuarios.", "Información", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
        }

        // Evento CellFormatting para enmascarar la contraseña
        private void dataGridView1_CellFormatting(object sender, DataGridViewCellFormattingEventArgs e)
        {
            if (dataGridView1.Columns[e.ColumnIndex].Name == "contraseña" && e.Value != null)
            {
                // Muestra la longitud de la contraseña con asteriscos
                e.Value = new string('*', e.Value.ToString().Length);
            }
        }

        private void btnNuevo_Click(object sender, EventArgs e)
        {
            CRUsuarios frm = new CRUsuarios();
            frm.ShowDialog();
        }

        private void btnEditar_Click(object sender, EventArgs e)
        {
            int usuarioId;

            if (dataGridView1.SelectedRows.Count > 0)
            {
                usuarioId = Convert.ToInt32(dataGridView1.SelectedRows[0].Cells["id"].Value);
                CRUsuarios cr = new CRUsuarios(usuarioId);
                cr.txtcontraseña.PasswordChar = '*';

                cr.txtid.Text = dataGridView1.CurrentRow.Cells[0].Value.ToString();               // ID
                cr.txtnombre.Text = dataGridView1.CurrentRow.Cells[1].Value.ToString();           // Nombre
                cr.txtcontraseña.Text = dataGridView1.CurrentRow.Cells[2].Value.ToString();       // Contraseña
                cr.txtcorreo.Text = dataGridView1.CurrentRow.Cells[3].Value.ToString();           // Correo


                cr.ShowDialog();
            }
            else
                MessageBox.Show("seleccione una fila por favor");
        }

        private async void button2_Click(object sender, EventArgs e)
        {
            // Verificar si hay una fila seleccionada en el DataGridView
            if (dataGridView1.SelectedRows.Count > 0)
            {
                // Obtener el ID del usuario seleccionado en el DataGridView
                long usuarioId = Convert.ToInt64(dataGridView1.SelectedRows[0].Cells["id"].Value);

                // Confirmar la eliminación con el usuario
                var confirmResult = MessageBox.Show("¿Estás seguro de que quieres eliminar este usuario?",
                                                     "Confirmación",
                                                     MessageBoxButtons.YesNo,
                                                     MessageBoxIcon.Question);
                if (confirmResult == DialogResult.Yes)
                {
                    // Instanciar la clase Administration
                    Administracion admin = new Administracion();

                    // Llamar al método para eliminar el usuario
                    bool isDeleted = await admin.EliminarUsuarioAsync(usuarioId);

                    if (isDeleted)
                    {
                        MessageBox.Show("Usuario eliminado correctamente.");

                        // Refrescar el DataGridView para quitar al usuario eliminado
                    }
                    else
                    {
                        MessageBox.Show("No se pudo eliminar el usuario.");
                    }
                }
            }
            else
            {
                MessageBox.Show("Por favor, selecciona un usuario para eliminar.");
            }
        }

        private void BtnCerrar_Click(object sender, EventArgs e)
        {
            this.Close();
        }
    }
}
