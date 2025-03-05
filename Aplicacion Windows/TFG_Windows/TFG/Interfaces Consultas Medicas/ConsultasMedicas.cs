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
    public partial class ConsultasMedicas : Form
    {
        public ConsultasMedicas()
        {
            InitializeComponent();
        }

        private void label1_Click(object sender, EventArgs e)
        {

        }

        private void BtnCerrar_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            
        }

        private async void ConsultasMedicas_Load(object sender, EventArgs e)
        {

            var appointments = await Administracion.ObtenerTodosLosAppointmentsAsync();
            if (appointments != null && appointments.Count > 0)
            {
                // proyectamos a un DTO que muestre usuario_id
                var data = appointments.Select(a => new
                {
                    a.id,
                    usuario_id = a.usuario != null ? a.usuario.id : 0,
                    a.descripcion,
                    a.fecha,
                    a.hora
                }).ToList();

                dataGridView1.DataSource = data;
                dataGridView1.Columns["usuario_id"].HeaderText = "Usuario ID";
            }
            else
            {
                MessageBox.Show("No se encontraron citas.");
            }
        }

        private void btnNuevo_Click(object sender, EventArgs e)
        {
            CRConsultasMedicas frm = new CRConsultasMedicas();
            frm.txtid.Text = "0"; // Indica modo Crear
            frm.ShowDialog();

            // Después de cerrar, recargar la lista de citas:
            ConsultasMedicas_Load(null, null);
        }

        private void btnEditar_Click(object sender, EventArgs e)
        {
            if (dataGridView1.SelectedRows.Count > 0)
            {
                CRConsultasMedicas frm = new CRConsultasMedicas();

                // Cargar los datos de la fila seleccionada en el CRAppointment
                frm.txtid.Text = dataGridView1.CurrentRow.Cells["id"].Value.ToString();
                frm.txtUsuario.Text = dataGridView1.CurrentRow.Cells["usuario_id"].Value.ToString();
                frm.txtDescripcion.Text = dataGridView1.CurrentRow.Cells["descripcion"].Value.ToString();

                // Parsear la fecha
                DateTime fecha;
                if (DateTime.TryParse(dataGridView1.CurrentRow.Cells["fecha"].Value.ToString(), out fecha))
                {
                    frm.dtpFecha.Value = fecha;
                }

                // Parsear la hora
                DateTime hora;
                if (DateTime.TryParse(dataGridView1.CurrentRow.Cells["hora"].Value.ToString(), out hora))
                {
                    frm.dtpHora.Value = hora;
                }

                frm.ShowDialog();
                ConsultasMedicas_Load(null, null); // Recargar la lista luego de editar
            }
            else
            {
                MessageBox.Show("Seleccione una cita para editar.", "Información", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            if (dataGridView1.SelectedRows.Count > 0)
            {
                CRConsultasMedicas frm = new CRConsultasMedicas();

                // Obtener valores del DataGridView
                string fechaString = dataGridView1.CurrentRow.Cells["fecha"].Value.ToString();
                string horaString = dataGridView1.CurrentRow.Cells["hora"].Value.ToString();

                // Parsear la fecha
                DateTime fechaParsed = DateTime.ParseExact(fechaString, "yyyy-MM-dd", System.Globalization.CultureInfo.InvariantCulture);
                // Parsear la hora
                DateTime horaParsed = DateTime.ParseExact(horaString, "HH:mm:ss", System.Globalization.CultureInfo.InvariantCulture);

                // Asignar valores a los controles del formulario CRAppointment
                frm.txtid.Text = dataGridView1.CurrentRow.Cells["id"].Value.ToString();
                frm.txtUsuario.Text = dataGridView1.CurrentRow.Cells["usuario_id"].Value.ToString();
                frm.txtDescripcion.Text = dataGridView1.CurrentRow.Cells["descripcion"].Value.ToString();

                // Asignar la fecha directamente
                frm.dtpFecha.Value = fechaParsed;

                // Asignar la hora con la fecha de hoy para no mostrar fecha en el control hora
                frm.dtpHora.Value = new DateTime(DateTime.Today.Year, DateTime.Today.Month, DateTime.Today.Day, horaParsed.Hour, horaParsed.Minute, horaParsed.Second);

                frm.ShowDialog();

                // Luego de cerrar el formulario, recargar la lista si es necesario
                ConsultasMedicas_Load(null, null);
            }
            else
            {
                MessageBox.Show("Seleccione una cita para editar.", "Información", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
        }
    }
}
