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

namespace TFG.Interfaces_Alarmas
{
    public partial class Alarmas : Form
    {
        public Alarmas()
        {
            InitializeComponent();
        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }

        private void BtnCerrar_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private async void Alarmas_Load(object sender, EventArgs e)
        {
            List<Reminders> reminders = await Administracion.ObtenerListaDeReminders();

            if (reminders != null && reminders.Count > 0)
            {
                var remindersDTO = reminders.Select(r => new RemindersDTO
                {
                    id = r.id,
                    medicamentos_id = r.medicamento_id.id,
                    hora = r.hora.ToString("HH:mm:ss"),
                    frecuencia = r.frecuencia,
                }).ToList();

                dataGridView1.DataSource = remindersDTO;

                if (dataGridView1.Columns.Contains("medicamento_id"))
                {
                    dataGridView1.Columns["medicamento_id"].HeaderText = "Medicamento_Id";
                    dataGridView1.Columns["medicamento_id"].ReadOnly = true;
                }
            }
            else
            {
                MessageBox.Show("No se encontraron Alarmas.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
        }
    }
}
