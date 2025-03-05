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
    public partial class CREstadoAnimo : Form
    {
        public CREstadoAnimo()
        {
            InitializeComponent();
        }

        private void BtnCerrar_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void CREstadoAnimo_Load(object sender, EventArgs e)
        {
            cbTipo.DataSource = Enum.GetValues(typeof(EstadoMental));

            if (string.IsNullOrEmpty(txtid.Text) || txtid.Text == "0")
            {
                button1.Visible = true;
                button2.Visible = false;
            }
            else
            {
                button1.Visible = false;
                button2.Visible = true;
            }
        }

        private async void button1_Click(object sender, EventArgs e)
        {
            try
            {
                long usuarioId = long.Parse(txtusuario.Text);

                MoodDTO nuevoMood = new MoodDTO
                {
                    usuario_id = usuarioId,
                    fecha = dtpFecha.Value.ToString("yyyy-MM-dd"),
                    estadoMental = cbTipo.SelectedItem.ToString(),
                    nota = txtDuracion.Text
                };

                var creado = await Administracion.CrearMoodAsync(nuevoMood);
                if (creado != null)
                {
                    MessageBox.Show("Estado de ánimo creado con éxito.");
                    this.Close();
                }
                else
                {
                    MessageBox.Show("No se pudo crear el estado de ánimo.");
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error: {ex.Message}");
            }
        }

        private async void button2_Click(object sender, EventArgs e)
        {
            try
            {
                long id = long.Parse(txtid.Text);
                long usuarioId = long.Parse(txtusuario.Text);

                MoodDTO moodEditado = new MoodDTO
                {
                    usuario_id = usuarioId,
                    fecha = dtpFecha.Value.ToString("yyyy-MM-dd"),
                    estadoMental = cbTipo.SelectedItem.ToString(),
                    nota = txtDuracion.Text
                };

                var editado = await Administracion.EditarMoodAsync(id, moodEditado);
                if (editado != null)
                {
                    MessageBox.Show("Estado de ánimo actualizado con éxito.");
                    this.Close();
                }
                else
                {
                    MessageBox.Show("No se pudo actualizar el estado de ánimo.");
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error: {ex.Message}");
            }
        }

        private void btnCancelar_Click(object sender, EventArgs e)
        {
            this.Close();
        }
    }
}
