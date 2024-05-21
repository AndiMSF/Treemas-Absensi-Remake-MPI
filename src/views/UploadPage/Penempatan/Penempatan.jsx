/* eslint-disable no-undef */
/* eslint-disable no-unused-vars */
import "./penempatan.css";
import Navbar from "../../../components/Content/Navbar/Navbar";
import Button from "../../../components/Elements/Buttons/Button";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import DataTable from "react-data-table-component";
import SortIcon from "@material-ui/icons/ArrowDownward";
import DataTableExtensions from "react-data-table-component-extensions";
import "react-data-table-component-extensions/dist/index.css";

import FormAbsen from "../../../components/Content/FormAbsen/FormAbsen";
import { Form } from "react-bootstrap";
const Penempatan = () => {
  const navigate = useNavigate();
  const [apiData, setApiData] = useState([]);
  const [titleForm, setTitleForm] = useState("");
  const [isTelatMasuk, setIsTelatMasuk] = useState(null);
  const [isAbsen, setIsAbsen] = useState(false);
  const [isToken, setIstoken] = useState("");
  const [showAbsenForm, setShowAbsenForm] = useState(false);
  const nik = localStorage.getItem("nik")
  const [formData, setFormData] = useState({
    active: null,
    projectId: ""
  })

  // State untuk menyimpan status switch
  const [switchStates, setSwitchStates] = useState({});

  // Get Data Absen
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch(
          "http://localhost:8081/api/master-data/project-view",
          {
            method: "GET", // Sesuaikan metode sesuai kebutuhan (GET, POST, dll.)
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${isToken}`, // Sertakan token di sini
            },
          }
        );
        const data = await response.json();
        if (data.status === "Success") {
          setApiData(data.data);
        } else {
          setError("Failed to fetch data");
        }
      } catch (error) {
        setError(`Error fetching data: ${error.message}`);
      }
    };

    const token = localStorage.getItem("authToken");
    if (token) {
      setIstoken(token);
      fetchData(); // Panggil fungsi fetchData setelah mendapatkan token
      console.log("Token: " + token);
    } else {
      navigate("/login");
    }
  }, [navigate, isToken]);

  const handleSwitchChange = (e, row) => {
    const { checked } = e.target;
    const { projectId } = row;

    // Update state switch berdasarkan projectId
    setSwitchStates((prevStates) => ({
      ...prevStates,
      [projectId]: checked,
    }));

    console.log(`Switch for ${projectId} is ${checked}`);
    setFormData({...formData,
      projectId: projectId,
      nik: nik,
      isActive: checked
    })

  };

  // Effect to log form data when it updates
useEffect(() => {
  console.log("Form Data ", formData);
}, [formData]);

  const columns = [
    {
      name: "Select",
      cell: (row) => (
        <Form.Check
          type="switch"
          id={`custom-switch-${row.projectId}`}
          checked={switchStates[row.projectId] || false}
          onChange={(e) => handleSwitchChange(e, row)}
        />
      ),
    },
    {
      name: "Project Id",
      selector: (row) => row.projectId || "-",
      cellExport: (row) => row.projectId || "-",
      sortable: true,
    },
    {
      name: "Nama Project",
      selector: (row) => row.namaProject || "-",
      cellExport: (row) => row.namaProject || "-",
      sortable: true,
    },
    {
      name: "Lokasi",
      selector: (row) => row.lokasi || "-",
      cellExport: (row) => row.lokasi || "-",
      sortable: true,
    },
    {
      name: "No Telepon",
      selector: (row) => row.noTlpn || "-",
      cellExport: (row) => row.noTlpn || "-",
      sortable: true,
    },
  ];

  const dataTable = {
    columns,
    data: apiData,
  };

  console.log(apiData);
  return (
    <div className="content__container">
      <Navbar navbarText="Upload / Penempatan" />
      <div className="input__container">
        <div className="left__container__input__absen">
          <div className="top__container__input">
            <div className="top__container__input__right"></div>
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

export default Penempatan;
