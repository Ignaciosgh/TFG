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

namespace TFG
{
    public partial class CRMedicamento : Form
    {
        public CRMedicamento()
        {
            InitializeComponent();
            cbDias.DataSource = Enum.GetValues(typeof(Dias));
            cmbEstado.DataSource = Enum.GetValues(typeof(Estado));
        }

        private void btnCancelar_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {

        }

        private async void button1_Click(object sender, EventArgs e)
        {
            try
            {
                long usuarioId = long.Parse(txtusuario.Text);
                Users usuario = await Administracion.ObtenerUsuarioPorIdAsync(usuarioId);
                if (usuario == null)
                {
                    MessageBox.Show("Usuario no encontrado.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return;
                }

                MedicationDTO medDTO = new MedicationDTO
                {
                    nombre = txtnombre.Text,
                    descripcion = txtdescripcion.Text,
                    usuario_id = usuario.id,
                    dias = (Dias)cbDias.SelectedItem,
                    comprimidos = int.Parse(textcomprimidos.Text),
                    veces_al_dia = int.Parse(textvecesDia.Text),
                    horas = texthoras.Text,
                    status = (Estado)cmbEstado.SelectedItem
                };

                var creado = await Administracion.CrearMedicamentoAsync(medDTO);
                if (creado != null)
                {
                    MessageBox.Show("Medicamento creado con éxito.", "Éxito", MessageBoxButtons.OK, MessageBoxIcon.Information);
                    this.Close();
                }
                else
                {
                    MessageBox.Show("No se pudo crear el medicamento.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void CRMedicamento_Load(object sender, EventArgs e)
        {
            // Determina si es creación o edición
            if (string.IsNullOrEmpty(txtid.Text) || txtid.Text == "0")
            {
                // Modo creación
                button1.Visible = true;
                button2.Visible = false;
            }
            else
            {
                // Modo edición
                button1.Visible = false;
                button2.Visible = true;
            }
        }

        private async void button2_Click(object sender, EventArgs e)
        {
            try
            {
                if (string.IsNullOrEmpty(txtid.Text) || txtid.Text == "0")
                {
                    MessageBox.Show("ID no válido para actualizar.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return;
                }

                long id = long.Parse(txtid.Text);
                long usuarioId = long.Parse(txtusuario.Text);
                Users usuario = await Administracion.ObtenerUsuarioPorIdAsync(usuarioId);
                if (usuario == null)
                {
                    MessageBox.Show("Usuario no encontrado.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return;
                }

                MedicationDTO medDTO = new MedicationDTO
                {
                    nombre = txtnombre.Text,
                    descripcion = txtdescripcion.Text,
                    usuario_id = usuario.id,
                    dias = (Dias)cbDias.SelectedItem,
                    comprimidos = int.Parse(textcomprimidos.Text),
                    veces_al_dia = int.Parse(textvecesDia.Text),
                    horas = texthoras.Text,
                    status = (Estado)cmbEstado.SelectedItem
                };

                var editado = await Administracion.EditarMedicamentoAsync(id, medDTO);
                if (editado != null)
                {
                    MessageBox.Show("Medicamento actualizado con éxito.", "Éxito", MessageBoxButtons.OK, MessageBoxIcon.Information);
                    this.Close();
                }
                else
                {
                    MessageBox.Show("No se pudo actualizar el medicamento.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }

        }

    }
}
