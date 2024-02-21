# README - Pill Dispense System App

The "Pill Dispense System" app is a comprehensive solution designed to enhance the interaction between doctors and patients regarding medication management. This system facilitates a seamless process for:

- **Creating Medication Plans**: Allows doctors to set up and modify medication plans for each patient. These plans can include multiple medications, with each one having a unique QR code for easy identification.
- **Database Management**: Stores medication plans, offering doctors the flexibility to update them as needed.
- **Versatile Patient Interaction**:
  - **Non-Logged In**: Patients can directly scan QR codes to begin the medication dispensing process. This mode operates without the need for patient login, embedding all necessary data within the QR code for immediate dispensing via an MQTT signal to our ESP 32 device.
  - **Logged-In**: Enhances patient engagement by enabling them to log in, view their medication history, and monitor their intake over time, including total pills taken and weekly intake statistics.
- **Access Control**: Distinct user roles ensure that patients have no access to the doctor panel. Additionally, any doctor attempting to log in via the patient interface is automatically redirected to QR code generation to minimize user errors.

For demonstration and testing purposes, we've provided preset login credentials:

- **Doctor's Access**:
  - Username: `docadmin`
  - Password: `admin`

- **Patient's Access (Example Patient)**:
  - Username: `patient0`
  - Password: `user`

The example patient, "patient0," comes with pre-generated data to demonstrate the app's functionality, particularly showcasing the bar chart feature in the patient overview section that visualizes the patient's medication adherence over the last ten weeks.
