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

namespace TFG.Interfaces_Actividad_Fisica
{
    public partial class CRActividadFisica : Form
    {

        private int _id;

        public CRActividadFisica(int id)
        {
            InitializeComponent();
            _id = id;
            cbTipo.DataSource = Enum.GetValues(typeof(TipoActividad));
        }

        public CRActividadFisica()
        {
            InitializeComponent();
            cbTipo.DataSource = Enum.GetValues(typeof(TipoActividad));
        }

        private void label5_Click(object sender, EventArgs e)
        {

        }

        private void textcomprimidos_TextChanged(object sender, EventArgs e)
        {

        }

        private void BarraTitulo_Paint(object sender, PaintEventArgs e)
        {

        }

        private void CRActividadFisica_Load(object sender, EventArgs e)
        {
            cbTipo.DataSource = Enum.GetValues(typeof(TipoActividad));

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

        private void btnCancelar_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void BtnCerrar_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private async void button1_Click(object sender, EventArgs e)
        {
            try
            {
                long usuarioId = long.Parse(txtusuario.Text);

                PhysicalActivityDTO nuevaActividad = new PhysicalActivityDTO
                {
                    usuario_id = usuarioId,
                    fecha = dtpFecha.Value.ToString("yyyy-MM-dd"),
                    tipo = cbTipo.SelectedItem.ToString(),
                    duracion = int.Parse(txtDuracion.Text)
                };

                var creada = await Administracion.CrearPhysicalActivityAsync(nuevaActividad);
                if (creada != null)
                {
                    MessageBox.Show("Actividad Física creada con éxito.");
                    this.Close();
                }
                else
                {
                    MessageBox.Show("No se pudo crear la actividad física.");
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

                PhysicalActivityDTO actividadEditada = new PhysicalActivityDTO
                {
                    usuario_id = usuarioId,
                    fecha = dtpFecha.Value.ToString("yyyy-MM-dd"),
                    tipo = cbTipo.SelectedItem.ToString(),
                    duracion = int.Parse(txtDuracion.Text)
                };

                var editada = await Administracion.EditarPhysicalActivityAsync(id, actividadEditada);
                if (editada != null)
                {
                    MessageBox.Show("Actividad Física actualizada con éxito.");
                    this.Close();
                }
                else
                {
                    MessageBox.Show("No se pudo actualizar la actividad física.");
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error: {ex.Message}");
            }
        }
    }
}
