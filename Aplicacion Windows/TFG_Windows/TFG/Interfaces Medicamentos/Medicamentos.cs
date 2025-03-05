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
    public partial class Medicamentos : Form
    {
        public Medicamentos()
        {
            InitializeComponent();
        }



        private void BtnCerrar_Click(object sender, EventArgs e)
        {
            this.Close();
        }


        private async void Medicamentos_Load(object sender, EventArgs e)
        {
            List<Medications> medications = await Administracion.ObtenerTodosLosMedicamentosAsync();

            if (medications != null && medications.Count > 0)
            {
                var medicationsDTO = medications
                    .Select(m => new MedicationDTO
                    {
                        id = m.id,
                        usuario_id = m.usuario.id, // Aquí tomamos el usuario.id
                        nombre = m.nombre,
                        descripcion = m.descripcion,
                        dias = m.dias,
                        comprimidos = m.comprimidos,
                        veces_al_dia = m.veces_al_dia,
                        horas = m.horas,
                        status = m.estado
                    }).ToList();

                dataGridView1.DataSource = medicationsDTO;

                if (dataGridView1.Columns.Contains("usuario_id"))
                {
                    dataGridView1.Columns["usuario_id"].HeaderText = "Usuario ID";
                    dataGridView1.Columns["usuario_id"].ReadOnly = true;
                }
            }
            else
            {
                MessageBox.Show("No se encontraron Medicamentos.", "Información", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
        }

        private void btnNuevo_Click(object sender, EventArgs e)
        {
            CRMedicamento frm = new CRMedicamento();
            frm.ShowDialog();

            // Luego de cerrar CRMedicamento, recargar la lista
            Medicamentos_Load(null, null);
        }

        private void btnEditar_Click(object sender, EventArgs e)
        {
            if (dataGridView1.SelectedRows.Count > 0)
            {
                CRMedicamento frm = new CRMedicamento();

                // Asumiendo que el DataGridView está vinculado a una lista de Medication (o MedicationDTO)
                // Aquí se recuperan los valores directamente del DataGridView.
                frm.txtid.Text = dataGridView1.CurrentRow.Cells["id"].Value.ToString();
                frm.txtnombre.Text = dataGridView1.CurrentRow.Cells["nombre"].Value.ToString();
                frm.txtdescripcion.Text = dataGridView1.CurrentRow.Cells["descripcion"].Value.ToString();
                frm.txtusuario.Text = dataGridView1.CurrentRow.Cells["usuario_id"].Value.ToString(); // Asumiendo que tienes una columna usuario_id
                frm.cbDias.SelectedItem = (Dias)dataGridView1.CurrentRow.Cells["dias"].Value;
                frm.textcomprimidos.Text = dataGridView1.CurrentRow.Cells["comprimidos"].Value.ToString();
                frm.textvecesDia.Text = dataGridView1.CurrentRow.Cells["veces_al_dia"].Value.ToString();
                frm.texthoras.Text = dataGridView1.CurrentRow.Cells["horas"].Value.ToString();
                frm.cmbEstado.SelectedItem = (Estado)dataGridView1.CurrentRow.Cells["status"].Value;

                // Aquí, como txtid > 0, en el Load se mostrará el botón Actualizar en lugar de Guardar
                frm.ShowDialog();

                // Después de cerrar el frm, puedes recargar la lista
                Medicamentos_Load(null, null);
            }
            else
            {
                MessageBox.Show("Por favor, seleccione un medicamento para editar.");
            }
        }

        private async void button2_Click(object sender, EventArgs e)
        {
            // Verifica si hay una fila seleccionada en el DataGridView
            if (dataGridView1.SelectedRows.Count > 0)
            {
                // Obtener el id del medicamento seleccionado
                long id = long.Parse(dataGridView1.SelectedRows[0].Cells["id"].Value.ToString());  // Asegúrate de que "id" es el nombre de la columna

                DialogResult dialogResult = MessageBox.Show("¿Estás seguro de que deseas eliminar este medicamento?", "Confirmación", MessageBoxButtons.YesNo);

                if (dialogResult == DialogResult.Yes)
                {
                    // Llamar al método para eliminar el medicamento
                    bool resultado = await Administracion.EliminarMedicamentoAsync(id);

                    if (resultado)
                    {
                        MessageBox.Show("Medicamento eliminado con éxito.");
                    }
                    else
                    {
                        MessageBox.Show("No se pudo eliminar el medicamento.");
                    }
                }
            }
            else
            {
                MessageBox.Show("Por favor, selecciona un medicamento para eliminar.", "Advertencia", MessageBoxButtons.OK, MessageBoxIcon.Warning);
            }
        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }
    }
}
