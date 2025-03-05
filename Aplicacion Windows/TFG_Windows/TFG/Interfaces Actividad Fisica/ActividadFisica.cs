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
using TFG.Enumerados;
using TFG.Interfaces;

namespace TFG.Interfaces_Actividad_Fisica
{
    public partial class ActividadFisica : Form
    {
        public ActividadFisica()
        {
            InitializeComponent();
        }

        private async void ActividadFisica_Load(object sender, EventArgs e)
        {
            var activities = await Administracion.ObtenerTodasLasActividadesFisicasAsync();
            if (activities != null && activities.Count > 0)
            {
                var data = activities.Select(a => new
                {
                    a.id,
                    usuario_id = a.usuario != null ? a.usuario.id : 0,
                    fecha = a.fecha.ToString("yyyy-MM-dd"), // `a.fecha` es DateTime en C#, conviértelo a string
                    tipo = a.tipo.ToString(), // convierte el enum a string
                    a.duracion
                }).ToList();

                dataGridView1.DataSource = data;
                dataGridView1.Columns["usuario_id"].HeaderText = "Usuario ID";
            }
            else
            {
                MessageBox.Show("No se encontraron Actividades Físicas.");
            }
        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }

        private void BtnCerrar_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private async void button2_Click(object sender, EventArgs e)
        {
            // Verificar si hay una fila seleccionada en el DataGridView
            if (dataGridView1.SelectedRows.Count > 0)
            {
                // Obtener el ID del usuario seleccionado en el DataGridView
                long actividadFisicaId = Convert.ToInt64(dataGridView1.SelectedRows[0].Cells["id"].Value);

                // Confirmar la eliminación con el usuario
                var confirmResult = MessageBox.Show("¿Estás seguro de que quieres eliminar esta actividad fisica?",
                                                     "Confirmación",
                                                     MessageBoxButtons.YesNo,
                                                     MessageBoxIcon.Question);
                if (confirmResult == DialogResult.Yes)
                {

                    // Llamar al método para eliminar el usuario
                    bool isDeleted = await Administracion.EliminarPhysicalActivities(actividadFisicaId);

                    if (isDeleted)
                    {
                        MessageBox.Show("Actividad fisica eliminada correctamente.");

                        // Refrescar el DataGridView para quitar al usuario eliminado
                    }
                    else
                    {
                        MessageBox.Show("No se pudo eliminar la actividad fisica.");
                    }
                }
            }
            else
            {
                MessageBox.Show("Por favor, selecciona una actividad fisica para eliminar.");
            }
        }

        private void btnEditar_Click(object sender, EventArgs e)
        {
            if (dataGridView1.SelectedRows.Count > 0)
            {
                CRActividadFisica frm = new CRActividadFisica();
                frm.txtid.Text = dataGridView1.CurrentRow.Cells["id"].Value.ToString();
                frm.txtusuario.Text = dataGridView1.CurrentRow.Cells["usuario_id"].Value.ToString();
                frm.txtDuracion.Text = dataGridView1.CurrentRow.Cells["duracion"].Value.ToString();

                // Parsear fecha
                DateTime fechaParsed;
                if (DateTime.TryParse(dataGridView1.CurrentRow.Cells["fecha"].Value.ToString(), out fechaParsed))
                {
                    frm.dtpFecha.Value = fechaParsed;
                }

                // Parsear tipo (enum)
                string tipoString = dataGridView1.CurrentRow.Cells["tipo"].Value.ToString();
                if (Enum.TryParse<TipoActividad>(tipoString, out TipoActividad tipoEnum))
                {
                    frm.cbTipo.SelectedItem = tipoEnum; // Asume que tienes un cbTipo con el enum
                }

                frm.ShowDialog();
                ActividadFisica_Load(null, null);
            }
            else
            {
                MessageBox.Show("Seleccione una Actividad Física para editar.");
            }
        }

        private void btnNuevo_Click(object sender, EventArgs e)
        {
            CRActividadFisica cr = new CRActividadFisica();
            cr.ShowDialog();
            ActividadFisica_Load(null, null); // recargar lista
        }

        private void label1_Click(object sender, EventArgs e)
        {

        }
    }
}
