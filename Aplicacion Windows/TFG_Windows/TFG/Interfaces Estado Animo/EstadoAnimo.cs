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

namespace TFG.Interfaces_Estado_Animo
{
    public partial class EstadoAnimo : Form
    {
        public EstadoAnimo()
        {
            InitializeComponent();
        }

        private async void EstadoAnimo_Load(object sender, EventArgs e)
        {
            var moods = await Administracion.ObtenerTodosLosMoodsAsync();
            if (moods != null && moods.Count > 0)
            {
                var data = moods.Select(m => new
                {
                    m.id,
                    usuario_id = m.usuario != null ? m.usuario.id : 0,
                    fecha = m.fecha.ToString("yyyy-MM-dd"),
                    estado = m.estado.ToString(),
                    m.nota
                }).ToList();

                dataGridView1.DataSource = data;
                dataGridView1.Columns["usuario_id"].HeaderText = "Usuario ID";
            }
            else
            {
                MessageBox.Show("No se encontraron estados de ánimo.");
            }
        }

        private void btnNuevo_Click(object sender, EventArgs e)
        {
            CREstadoAnimo frm = new CREstadoAnimo();
            frm.ShowDialog();
            EstadoAnimo_Load(null, null);
        }

        private void btnEditar_Click(object sender, EventArgs e)
        {
            if (dataGridView1.SelectedRows.Count > 0)
            {
                CREstadoAnimo frm = new CREstadoAnimo();
                frm.txtid.Text = dataGridView1.CurrentRow.Cells["id"].Value.ToString();
                frm.txtusuario.Text = dataGridView1.CurrentRow.Cells["usuario_id"].Value.ToString();
                frm.txtDuracion.Text = dataGridView1.CurrentRow.Cells["nota"].Value.ToString();

                // Parsear fecha
                DateTime fechaParsed;
                if (DateTime.TryParse(dataGridView1.CurrentRow.Cells["fecha"].Value.ToString(), out fechaParsed))
                {
                    frm.dtpFecha.Value = fechaParsed;
                }

                // Parsear estado
                string estadoString = dataGridView1.CurrentRow.Cells["estado"].Value.ToString();
                if (Enum.TryParse<EstadoMental>(estadoString, out EstadoMental estadoEnum))
                {
                    frm.cbTipo.SelectedItem = estadoEnum;
                }

                frm.ShowDialog();
                EstadoAnimo_Load(null, null);
            }
            else
            {
                MessageBox.Show("Seleccione un estado de ánimo para editar.");
            }
        }

        private async void button2_Click(object sender, EventArgs e)
        {
            if (dataGridView1.SelectedRows.Count > 0)
            {
                long id = (long)dataGridView1.CurrentRow.Cells["id"].Value;
                DialogResult confirm = MessageBox.Show("¿Eliminar este estado de ánimo?", "Confirmar", MessageBoxButtons.YesNo);
                if (confirm == DialogResult.Yes)
                {
                    bool eliminado = await Administracion.EliminarMoodAsync(id);
                    if (eliminado)
                    {
                        MessageBox.Show("Estado de ánimo eliminado.");
                        EstadoAnimo_Load(null, null);
                    }
                    else
                    {
                        MessageBox.Show("No se pudo eliminar el estado de ánimo.");
                    }
                }
            }
            else
            {
                MessageBox.Show("Seleccione un estado de ánimo para eliminar.");
            }
        }
    }
}
