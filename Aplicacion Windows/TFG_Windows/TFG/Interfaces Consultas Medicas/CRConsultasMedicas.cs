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

namespace TFG.Interfaces_Consultas_Medicas
{
    public partial class CRConsultasMedicas : Form
    {
        public CRConsultasMedicas()
        {
            InitializeComponent();
        }

        private void label6_Click(object sender, EventArgs e)
        {

        }

        private void label7_Click(object sender, EventArgs e)
        {

        }

        private void BarraTitulo_Paint(object sender, PaintEventArgs e)
        {

        }

        // Boton Guardar
        private async void button1_Click(object sender, EventArgs e)
        {

            long usuarioId = long.Parse(txtid.Text);

            AppointmentDTO nuevaCita = new AppointmentDTO
            {
                usuario_id = usuarioId,
                descripcion = txtDescripcion.Text,
                fecha = dtpFecha.Value.ToString("yyyy-MM-dd"),
                hora = dtpHora.Value.ToString("HH:mm:ss")
            };

            
            Users usuario = await Administracion.ObtenerUsuarioPorIdAsync(usuarioId);
            if (usuario == null)
            {
                MessageBox.Show("Usuario no encontrado.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }

            var creada = await Administracion.CrearAppointmentAsync(nuevaCita);
            if (creada != null)
            {
                MessageBox.Show("Cita creada con éxito.");
                this.Close(); // Cerrar el formulario de creación
            }
            else
            {
                MessageBox.Show("No se pudo crear la cita.");
            }
        }

        // Boton Actualizar
        private async void button2_Click(object sender, EventArgs e)
        {
            long id = long.Parse(txtid.Text);
            long usuarioId = long.Parse(txtUsuario.Text);
            Users usuario = await Administracion.ObtenerUsuarioPorIdAsync(usuarioId);
            if (usuario == null)
            {
                MessageBox.Show("Usuario no encontrado.");
                return;
            }

            AppointmentDTO citaEditada = new AppointmentDTO
            {
                usuario_id = usuarioId,
                descripcion = txtDescripcion.Text,
                fecha = dtpFecha.Value.ToString("yyyy-MM-dd"),
                hora = dtpHora.Value.ToString("HH:mm:ss")
            };

            var editada = await Administracion.EditarAppointmentAsync(id, citaEditada);
            if (editada != null)
            {
                MessageBox.Show("Cita actualizada con éxito.");
                this.Close();
            }
            else
            {
                MessageBox.Show("No se pudo actualizar la cita.");
            }
        }

        // Boton Cancelar
        private void btnCancelar_Click(object sender, EventArgs e)
        {
            Close();
        }

        // Boton Cerrar
        private void BtnCerrar_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void label1_Click(object sender, EventArgs e)
        {

        }

        private void CRConsultasMedicas_Load(object sender, EventArgs e)
        {
            dtpHora.Format = DateTimePickerFormat.Custom;
            dtpHora.CustomFormat = "HH:mm:ss";
            dtpHora.ShowUpDown = true;

            // Determinar modo (crear o editar)
            if (string.IsNullOrEmpty(txtid.Text) || txtid.Text == "0")
            {
                // Modo crear
                button1.Visible = true;
                button2.Visible = false;
            }
            else
            {
                // Modo editar
                button1.Visible = false;
                button2.Visible = true;
            }
        }
    }
}
