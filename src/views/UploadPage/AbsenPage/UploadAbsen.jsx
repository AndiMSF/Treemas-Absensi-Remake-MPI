/* eslint-disable no-undef */
/* eslint-disable no-unused-vars */
import "./absen.css";
import Navbar from "../../../components/Content/Navbar/Navbar";
import Button from "../../../components/Elements/Buttons/Button";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import DataTable from "react-data-table-component";
import SortIcon from "@material-ui/icons/ArrowDownward";
import DataTableExtensions from "react-data-table-component-extensions";
import "react-data-table-component-extensions/dist/index.css";

import FormAbsen from "../../../components/Content/FormAbsen/FormAbsen";
const UploadAbsen = () => {
  const navigate = useNavigate();
  const [apiData, setApiData] = useState([]);
  const [titleForm, setTitleForm] = useState("");
  const [isTelatMasuk, setIsTelatMasuk] = useState(null);
  const [isAbsen, setIsAbsen] = useState(false);
  const [isAbsenMasuk, setIsAbsenMasuk] = useState(false)
  const [isAbsenPulang, setIsAbsenPulang] = useState(false)
  const [isAbsenPulangCepat, setIsAbsenPulangCepat] = useState(false)
  const [isAbsenLupaPulang, setIsAbsenLupaPulang] = useState(false)
  const [showAbsenForm, setShowAbsenForm] = useState(false);
  
  const handleShow = () => setShowAbsenForm(true);
  const handleClose = (isAbsen) => {
    setShowAbsenForm(false);
    setIsAbsen(isAbsen);
    localStorage.removeItem("alamatProject");
    console.log("HI SAYA SUDAH ABSEN");
  };

  const isAbsenMasukFunc = (isAbsenMasukStorage) => {
    if(isAbsenMasukStorage === true) {
      localStorage.setItem("isAbsenMasuk", true)
      setIsAbsenMasuk(isAbsenMasukStorage)
      console.log("TRUE");
    } else {
      localStorage.removeItem("isAbsenMasuk")
      console.log("NO TRUE");
    }

  }

  const isAbsenMasukLocalStorage = localStorage.getItem("isAbsenMasuk")


  useEffect(() => {
  }, [isAbsenMasukLocalStorage, isAbsenMasukFunc])

  const handleAbsenMasuk = () => {
    const now = new Date();
    const currentHour = now.getHours();
    console.log(currentHour);
    if (currentHour >= 9) {
      setShowAbsenForm(true);
      setTitleForm("Absen Telat Masuk");
      setIsTelatMasuk(true);
    } else {
      setShowAbsenForm(true);
      setTitleForm("Absen Masuk");
      setIsTelatMasuk(false);
    }
  };

  const handleAbsenPulang = () => {
    const now = new Date();
    const currentHour = 4;
    console.log(currentHour);
    
    if (currentHour >= 9 && currentHour < 18) {
        setShowAbsenForm(true);
        setTitleForm("Absen Pulang Cepat");
        setIsAbsenPulangCepat(true)
        setIsAbsenPulang(false)
        setIsAbsenLupaPulang(false)
    } else if (currentHour >= 18 && currentHour < 24) {
        setShowAbsenForm(true);
        setTitleForm("Absen Pulang");
        setIsAbsenPulang(true)
        setIsAbsenPulangCepat(false)
        setIsAbsenLupaPulang(false)
    } else if (currentHour >= 0 && currentHour < 9) {
        setShowAbsenForm(true);
        setTitleForm("Absen Lupa Pulang");
        setIsAbsenLupaPulang(true)
        setIsAbsenPulangCepat(false)
        setIsAbsenPulang(false)
    }
};

  // Get Data Absen
  useEffect(() => {
    const fetchData = async () => {
      console.log("MASUK USE EFFECT");
      try {
        const response = await fetch(
          "http://localhost:8081/api/upload/get-absen",
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
        if (data.success === true) {
          setApiData(data.data);
          console.log(data);
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
    if (token) {
      fetchData(); // Panggil fungsi fetchData setelah mendapatkan token
    } else {
      navigate("/login");
    }
  }, [navigate]);

  const columns = [
    {
      name: "NIK",
      selector: (row) => row.nik || "-",
      cellExport: (row) => row.nik || "-",
      sortable: true,
    },
    {
      name: "Nama Lengkap",
      selector: (row) => row.nama || "-",
      cellExport: (row) => row.nama || "-",
      sortable: true,
    },
    {
      name: "Tanggal",
      selector: (row) => row.tglAbsen || "-",
      cellExport: (row) => row.tglAbsen || "-",
      sortable: true,
    },
    {
      name: "Project",
      selector: (row) => row.projectId.namaProject || "-",
      cellExport: (row) => row.projectId.namaProject || "-",
      sortable: true,
    },
    {
      name: "Status",
      selector: (row) => {
        if (row.isApprove === "1") {
          return "Disetujui";
        } else if (row.isApprove === "0") {
          return "Ditolak";
        } else {
          return "Menunggu";
        }
      },
      cellExport: (row) => {
        if (row.isApprove === "1") {
          return "Disetujui";
        } else if (row.isApprove === "0") {
          return "Ditolak";
        } else {
          return "-";
        }
      },
      sortable: true,
    },
  ];

  const dataTable = {
    columns,
    data: apiData,
  };
  return (
    <div className="content__container">
      <Navbar navbarText="Upload / Absen" />
      {showAbsenForm && (
        <FormAbsen
          title={titleForm}
          show={showAbsenForm}
          handleClose={handleClose}
          isTelatMasuk={isTelatMasuk}
          isAbsenMasukFunc={isAbsenMasukFunc}
          isAbsenPulang={isAbsenPulang}
          isAbsenPulangCepat={isAbsenPulangCepat}
          isAbsenLupaPulang={isAbsenLupaPulang}
          isAbsenMasuk={isAbsenMasuk}
        />
      )}
      <div className="input__container">
        <div className="left__container__input__absen">
          <div className="top__container__input">
            <div className="top__container__input__right">
              
              {isAbsenMasukLocalStorage ? (
                <Button
                  text="Absen Pulang"
                  className="unggah__button"
                  onClick={handleAbsenPulang}
                />
              ) : <Button
              text="Absen Masuk"
              className="unggah__button"
              onClick={handleAbsenMasuk}
            />}

              {/* <ExportToExcel apiData={apiData} fileName={"Template_Absent_Treemas"}/> */}
            </div>
          </div>
        </div>
      </div>
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
    </div>
  );
};

export default UploadAbsen;
