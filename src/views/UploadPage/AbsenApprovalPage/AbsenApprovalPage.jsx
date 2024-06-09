/* eslint-disable no-unused-vars */
import Navbar from "../../../components/Content/Navbar/Navbar";
import Button from "../../../components/Elements/Buttons/Button";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import DataTable from "react-data-table-component";
import SortIcon from "@material-ui/icons/ArrowDownward";
import DataTableExtensions from "react-data-table-component-extensions";
import "react-data-table-component-extensions/dist/index.css";
import Swal from "sweetalert2";

const AbsenApprovalPage = () => {
  const navigate = useNavigate();
  const [apiData, setApiData] = useState([]);

  // Get Data Absen
  useEffect(() => {
    const fetchData = async () => {
      console.log("MASUK USE EFFECT");
      try {
        const response = await fetch(
          "http://localhost:8081/api/absen/absen-approval",
          {
            method: "GET", // Sesuaikan metode sesuai kebutuhan (GET, POST, dll.)
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`, // Sertakan token di sini
            },
          }
        );
        // Tambahkan penanganan kesalahan di sini
        const data = await response.json();
        console.log(data.status);
        if (data.status === "Success") {
          setApiData(data.data);
          console.log(
            "SUCCESS INI DATANYA " + JSON.stringify(data.data, null, 2)
          );
        }
      } catch (error) {
        if (error.message.includes("HTTP error!")) {
          const statusCode = parseInt(error.message.split(" ").pop());
          console.log("Status Code:", statusCode);

          if (statusCode === 401) {
            console.log("Masuk 401");
            // Token expired, remove token from local storage and redirect to login
            localStorage.removeItem("authToken");
          } else {
            console.log("Gak Masuk 401");
          }
        } else {
          console.log("Gak Masuk 401");
        }
      }
    };

    const token = localStorage.getItem("authToken");
    console.log("INI TOKEN "+token);
    if (token) {
      fetchData(); // Panggil fungsi fetchData setelah mendapatkan token
    } else {
      navigate("/login");
    }
  }, [navigate]);

  const getRowColor = (status) => {
    console.log("STATUS " + status);
    switch (status) {
      case "DISETUJUI":
        return {
          backgroundColor: "#AAD9BB",
          color: "#000000",
          borderRadius: "5px",
          padding: "5px",
          width: "100%",
          textAlign: "center",
          fontWeight: "600",
        };
      case "DITOLAK":
        return {
          backgroundColor: "#E74646",
          color: "#000000",
          borderRadius: "5px",
          padding: "5px",
          width: "100%",
          textAlign: "center",
          fontWeight: "600",
        };
      case "MENUNGGU":
        return {
          backgroundColor: "#FAEF9B",
          color: "#000000",
          borderRadius: "5px",
          padding: "5px",
          width: "100%",
          textAlign: "center",
          fontWeight: "600",
        };
      default:
        return {}; // Default style
    }
  };

  const handleClick = (id) => {
    console.log(`approve button clicked for ID: ${id}`);
    // Tambahkan logika penghapusan data di sini, atau panggil API approve jika diperlukan
    Swal.fire({
      title: "Are you sure?",
      text: "You won't be able to revert this!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Yes, approve it!",
    }).then(async (result) => {
      if (result.isConfirmed) {
        // Jika yes akan
        const token = localStorage.getItem("authToken");
        if (!token) {
          console.error("Token is not available");
          return;
        }

        try {
          const response = await fetch(
            `http://localhost:8081/api/absen/approve/${id}`,
            {
              method: "PUT",
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
              },
            }
          );

          const data = await response.json();
          if (response.ok) {
            // Berhasil dihapus
            Swal.fire({
              title: "Approved!",
              text: "Absen has been approved.",
              icon: "success",
            });
            setApiData((prevData) => prevData.filter((item) => item.id !== id));
          } else {
            // Gagal dihapus
            Swal.fire({
              title: "Error!",
              text: data.message, // Tampilkan pesan error dari server
              icon: "error",
            });
          }
        } catch (error) {
          // Error selama proses penghapusan
          console.error("Error approving data:", error);
          Swal.fire({
            title: "Error!",
            text: "An error occurred while approving the data.",
            icon: "error",
          });
        }
      }
    });
  };

  const columns = [
    {
      name: "NIK",
      selector: (row) => row.nik || "-",
      cellExport: (row) => row.nik || "-",
      sortable: true,
    },
    {
      name: "Nama Karyawan",
      selector: (row) => row.namaKaryawan || "-",
      cellExport: (row) => row.namaKaryawan || "-",
      sortable: true,
    },
    {
      name: "Project",
      selector: (row) => row.projectId?.namaProject || "-",
      cellExport: (row) => row.projectId?.namaProject || "-",
      sortable: true,
    },
    {
      name: "Lokasi Masuk",
      selector: (row) => row.lokasiMasuk || "-",
      cellExport: (row) => row.lokasiMasuk || "-",
      sortable: true,
    },
    {
      name: "Jam Masuk",
      selector: (row) => row.jamMasuk || "-",
      cellExport: (row) => row.jamMasuk || "-",
      sortable: true,
    },
    {
      name: "Lokasi Pulang",
      selector: (row) => row.lokasiPulang || "-",
      cellExport: (row) => row.lokasiPulang || "-",
      sortable: true,
    },
    {
      name: "Jam Pulang",
      selector: (row) => row.jamPulang || "-",
      cellExport: (row) => row.jamPulang || "-",
      sortable: true,
    },
    {
      name: "Catatan Terlambat",
      selector: (row) => row.catatanTerlambat || "-",
      cellExport: (row) => row.catatanTerlambat || "-",
      sortable: true,
    },
    {
      name: "Total Jam Kerja",
      selector: (row) => row.totalJamKerja || "-",
      cellExport: (row) => row.totalJamKerja || "-",
      sortable: true,
    },
    {
      name: "Status",
      selector: (row) => {
        if (row.isAbsen === "1") {
          return "DISETUJUI";
        } else if (row.isAbsen === "0") {
          return "DITOLAK";
        } else {
          return "MENUNGGU";
        }
      },
      cell: (row) => {
        const status =
          row.isAbsen === "1"
            ? "DISETUJUI"
            : row.isAbsen === "0"
            ? "DITOLAK"
            : "MENUNGGU";
        return <div style={getRowColor(status)}>{status}</div>;
      },
      sortable: true,
    },
    {
      name: "Action",
      sortable: false,
      cell: (d) => (
          <i
            key={`approve-${d.id}`}
            onClick={() => handleClick(d.id)}
            style={{ cursor: "pointer" }}
            className="first fas fa-check-square"
          ></i>
      ),
    },
  ];

  const dataTable = {
    columns,
    data: apiData,
  };
  return <div className="content__container">
  <Navbar navbarText="Upload / Absen Approval" />
  <div className="table__container">
    <DataTableExtensions {...dataTable}>
      <DataTable
        columns={columns}
        data={apiData}
        noHeader
        defaultSortField="nik"
        sortIcon={<SortIcon />}
        defaultSortAsc={true}
        pagination
        highlightOnHover
        dense
      />
    </DataTableExtensions>
  </div>
</div>;
};

export default AbsenApprovalPage;
