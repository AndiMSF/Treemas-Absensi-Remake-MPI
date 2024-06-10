/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import "./formAbsen.css";
import { useEffect, useState } from "react";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import Modal from "react-bootstrap/Modal";
import DropdownMenu from "../../Elements/DropdownMenu/DropdownMenu";
import { Dropdown, FormControl } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import Swal from "sweetalert2";

const FormAbsen = ({
  show,
  handleClose,
  title,
  isTelatMasuk,
  isAbsenMasukFunc,
  isAbsenPulang,
  isAbsenPulangCepat,
  isAbsenLupaPulang,
}) => {
  const navigate = useNavigate();
  const [dropdownProjectTitle, setDropdownProjectTitle] =
    useState("Pilih Project");
  const [projectData, setProjectData] = useState([]);
  const [project, setProject] = useState("");
  const [isToken, setIstoken] = useState("");
  const [error, setError] = useState(null);
  const [selectedWfhOnsite, setSelectedWfhOnSite] = useState("WFH / On Site");
  const [alamatProject, setAlamatProject] = useState(
    localStorage.getItem("alamatProject") || ""
  );
  const [loading, setLoading] = useState(false);
  const [valueLokasiWFH, setValueLokasiWFH] = useState("");
  const [catatanTelatMsk, setCatatanTelatMsk] = useState("");
  const [lokasiPlg, setLokasiPlg] = useState("")
  const [valueAbsen, setValueAbsen] = useState("");
  const [position, setPosition] = useState({ latitude: null, longitude: null });
  let isAbsenMasuk = localStorage.getItem("isAbsenMasuk");
  const [catatanLupaPlg, setCatatanLupaPlg] = useState("")
  const [catatanPlgCepat, setCatatanPlgCepat]= useState("")
  const [timesheet, setTimesheet] = useState("")
  const [formData, setFormData] = useState({
    projectId: "",
    nik: "",
    nama: "",
    hari: "",
    tgl_absen: null,
    image64: "",
    image: "",
    gpsLatitudeMsk: "",
    gpsLongitudeMsk: "",
    isWfh: "",
  });

  const [jamPlgLupaPlg, setJamPlgLupaPlg] = useState("")

  console.log(isTelatMasuk);

  const handleProject = (selectedProject) => {
    console.log("MASUK HANDLE PROJECT");
    // Cari data jabatan berdasarkan selected project yaitu nama project
    const selectedItem = projectData.find(
      (jabatan) => jabatan.namaProject === selectedProject
    );
    console.log("Project  " + JSON.stringify(selectedItem, null, 2));
    setDropdownProjectTitle(selectedProject);
    // kalau sudah ketemu set project value nya itu adalah projectId, untuk namaProject berlaku diatas, hanya untuk menampilkan namaProject jika user klik diantara project, kalau ke database kita pakai projectId untuk mengirim nya.
    setProject(selectedItem.projectId);
    // Update formData with GPS coordinates
    setFormData((prevFormData) => ({
      ...prevFormData,
      gpsLatitudeMsk: selectedItem.gps_latitude,
      gpsLongitudeMsk: selectedItem.gps_longitude,
    }));

    // set alamat dari project ke localstorage dan state
    const alamat = selectedItem.lokasi;
    localStorage.setItem("alamatProject", alamat);
    setAlamatProject(alamat);

    // Update valueAbsen jika bukan WFH
    if (selectedWfhOnsite !== "WFH / On Site") {
      setValueAbsen(alamat);
    }
  };

  const handleWfhOnsite = (selectedWfhOnsite) => {
    setSelectedWfhOnSite(selectedWfhOnsite);

    if (selectedWfhOnsite === "WFH / On Site") {
      setValueAbsen("");
    } else if (selectedWfhOnsite === "WFH") {
      setValueAbsen("");
      // Update formData with GPS coordinates
      setFormData((prevFormData) => ({
        ...prevFormData,
        gpsLatitudeMsk: "",
        gpsLongitudeMsk: "",
        isWfh: "1",
      }));
    } else {
      setValueAbsen(alamatProject);
      // setFormData( ...formData, {
      //   gpsLatitudeMsk:
      // })
      console.log("MASUK ON SITE");
      setFormData((prevFormData) => ({
        ...prevFormData,
        isWfh: "0",
      }));
    }
  };

  const handleChange = (event) => {
    setValueLokasiWFH(event.target.value);
    setValueAbsen(event.target.value);
  };

  const handleCatatanTltMskChange = (event) => {
    setCatatanTelatMsk(event.target.value);
    
  };

  const handleTimesheetChange = (event) => {
    setTimesheet(event.target.value)

  }

  const handleCatatanPlgCepat = (event) => {
    setCatatanPlgCepat(event.target.value)

  }

  const handleLokasiPlgChange = (event) => {
    setLokasiPlg(event.target.value)

  }

  const handleCatatanLupaPlg = (event) => {
    setCatatanLupaPlg(event.target.value)

  }

  const handleJamPlgLupaPlg = (event) => {
    setJamPlgLupaPlg(event.target.value)
  }

  const idAbsen  = localStorage.getItem("idAbsen")

  // get Project
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch(
          "http://localhost:8081/update-list-projects",
          {
            method: "GET", // Sesuaikan metode sesuai kebutuhan (GET, POST, dll.)
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${isToken}`, // Sertakan token di sini
            },
          }
        );
        const data = await response.json();
        if (data.success === true) {
          // Filter data yang memiliki isActive = 1
          const filteredData = data.data.filter(
            (item) => item.isActive === "1"
          );
          setProjectData(filteredData);
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
  }, [navigate, project, isToken, isAbsenMasuk]);

  const handleImageUpload = (e) => {
    const file = e.target.files[0]; // Mendapatkan file yang diunggah dari input

    if (file) {
      const reader = new FileReader();
      reader.onload = (event) => {
        // Konversi gambar ke base64 dan simpan dalam state formData
        setFormData({
          ...formData,
          image64: event.target.result,
          image: file.name,
        });
      };
      reader.readAsDataURL(file);
    }
  };

  const handleSaveChanges = async (e) => {
    // agar tombol absen pulang ada kita lempar true ke parent component
    // Harusnya dari Database
    handleClose(true);

    e.preventDefault();
    const token = localStorage.getItem("authToken");
    setLoading(true);

    if (!token) {
      console.error("Token is not available");
      return navigate("/login");
    }

    try {
      let requestData;
      if (isAbsenPulangCepat) {
        requestData = {
          notePekerjaan: timesheet,
          lokasiPlg: lokasiPlg,
          notePlgCepat: catatanPlgCepat,
        };
      } else if (isAbsenPulang) {
        requestData = {
          notePekerjaan: timesheet,
          lokasiPlg: lokasiPlg,
        };
      } else if (isAbsenLupaPulang) {
        requestData = {
          keteranganLupaPulang: catatanLupaPlg,
          notePekerjaan: timesheet,
          lokasiPlg: lokasiPlg,
          jamPlg: jamPlgLupaPlg,
        };
      } else {
        requestData = {
        projectId: project,
        hari: formData.hari,
        lokasiMsk: valueAbsen,
        gpsLatitudeMsk: formData.gpsLatitudeMsk,
        gpsLongitudeMsk: formData.gpsLongitudeMsk,
        isWfh: formData.isWfh,
        noteTelatMsk: catatanTelatMsk,
        photoAbsen: formData.image64,
        image: formData.image
      }
    }

      console.log(requestData);
      console.log("Token " + token);

      let response;
      if (isAbsenPulang) {
        response = await axios.post(
          "http://localhost:8081/api/absen/input-absen-pulang",
          requestData,
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
          }
        );

        
      } else if (isAbsenPulangCepat) {
        response = await axios.post(
          "http://localhost:8081/api/absen/input-absen-pulang",
          requestData,
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
          }
        );
      } else if (isAbsenLupaPulang) {
        response = await axios.post(
          `http://localhost:8081/api/absen/input-absen-belum-pulang?id=${idAbsen}`,
          requestData,
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
          }
        );
      } else {
        response = await axios.post(
          "http://localhost:8081/api/absen/input-absen",
          requestData,
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
          }
        );

        localStorage.setItem("idAbsen", response.data.data.id)

      }

      Swal.fire({
        title: "Success!",
        text: "Absen Submitted.",
        icon: "success",
      });
      setLoading(false);
      console.log("Response from API:", JSON.stringify(response,null,2));
      if (isAbsenPulangCepat || isAbsenPulang || isAbsenLupaPulang) {
        isAbsenMasukFunc(false);
      } else {
        isAbsenMasukFunc(true)
      }
      navigate('/upload/absen')
      
    } catch (error) {
      // Jika tidak berhasil, tampilkan pesan error
      Swal.fire({
        title: "Error!",
        text: "Failed to Absen.",
        icon: "error",
      });

      console.log(error);
      setLoading(false);
    }
  };

  console.log(isAbsenPulangCepat);

  return (
    <Modal show={show} onHide={handleClose}>
      <Modal.Header closeButton>
        <Modal.Title>{title}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form>
          {isAbsenPulangCepat || isAbsenPulang || isAbsenLupaPulang ? (
            ''
          ): <>
          <Dropdown
          onSelect={handleProject}
          className="user__access__dropdown"
          style={{ width: "100%" }}
        >
          <Dropdown.Toggle variant="primary" id="dropdown-basic">
            {dropdownProjectTitle}
          </Dropdown.Toggle>

          <Dropdown.Menu>
            {projectData.map((item, index) => (
              <Dropdown.Item key={index} eventKey={item.namaProject}>
                {item.namaProject}
              </Dropdown.Item>
            ))}
          </Dropdown.Menu>
        </Dropdown>
        <DropdownMenu
          onDropdownChange={handleWfhOnsite}
          items={["WFH", "On Site"]}
          title={selectedWfhOnsite}
        />

        <FormControl
          disabled={selectedWfhOnsite === "WFH" ? false : true}
          type="text"
          placeholder="Lokasi Absen"
          value={selectedWfhOnsite === "WFH" ? valueLokasiWFH : valueAbsen}
          onChange={handleChange}
        />
        </>}
          

          {isTelatMasuk ? (
            <Form.Group
              className="mb-3"
              controlId="exampleForm.ControlTextarea1"
            >
              <Form.Label style={{ color: "black" }}>
                Catatan telat masuk
              </Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                value={catatanTelatMsk}
                onChange={handleCatatanTltMskChange}
              />
            </Form.Group>
          ): ''}

          {/* Catatan Pulang Cepat */}
          {isAbsenPulangCepat && (
            <Form.Group
              className="mb-3"
              controlId="exampleForm.ControlTextarea1"
            >
              <Form.Label style={{ color: "black" }}>
                Catatan Pulang Cepat
              </Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                value={catatanPlgCepat}
                onChange={handleCatatanPlgCepat}
              />
            </Form.Group>
          )}

          {/* Catatan Pulang Cepat */}
          {isAbsenPulangCepat || isAbsenPulang || isAbsenLupaPulang ? (
            <Form.Group
              className="mb-3"
              controlId="exampleForm.ControlTextarea1"
            >
              <Form.Label style={{ color: "black" }}>
                Timesheet
              </Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                value={timesheet}
                onChange={handleTimesheetChange}
              />
            </Form.Group>
          ) : ''}
         
          {/* Catatan Lupa Pulang */}
          {isAbsenLupaPulang && (
            <Form.Group
              className="mb-3"
              controlId="exampleForm.ControlTextarea1"
            >
              <Form.Label style={{ color: "black" }}>
                Catatan Lupa Pulang
              </Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                value={catatanLupaPlg}
                onChange={handleCatatanLupaPlg}
              />
            </Form.Group>
          )}
          

          {/* LokasiPlg */}
          {isAbsenPulangCepat || isAbsenPulang || isAbsenLupaPulang && (
            <Form.Group
              className="mb-3"
              controlId="exampleForm.ControlTextarea1"
            >
              <Form.Label style={{ color: "black" }}>
                Lokasi Pulang
              </Form.Label>
              <Form.Control
                as="textarea"
                rows={2}
                value={lokasiPlg}
                onChange={handleLokasiPlgChange}
              />
            </Form.Group>
          )}

          {isAbsenLupaPulang && (
            <FormControl
            type="text"
            placeholder="Jam Pulang"
            value={jamPlgLupaPlg}
            onChange={handleJamPlgLupaPlg}
          />
          )}

          {selectedWfhOnsite === "WFH" && (
            <Form.Group className="upload" controlId="formFile">
              <div className="form__row__left">
                <Form.Label>
                  Foto WFH
                </Form.Label>
              </div>
              <div className="form__row__right">
                <Form.Control type="file" onChange={handleImageUpload} />
              </div>
            </Form.Group>
          )}
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        <Button variant="primary" onClick={handleSaveChanges}>
          Save Changes
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default FormAbsen;
