import "./formAbsen.css";
import { useEffect, useState } from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';
import DropdownMenu from "../../Elements/DropdownMenu/DropdownMenu";
import { Dropdown, FormControl } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import Swal from "sweetalert2";

const FormAbsen = ({ show, handleClose, title, isTelatMasuk }) => {
  const navigate = useNavigate();
  const [dropdownProjectTitle, setDropdownProjectTitle] = useState('Pilih Project');
  const [projectData, setProjectData] = useState([]);
  const [project, setProject] = useState("");
  const [isToken, setIstoken] = useState("");
  const [error, setError] = useState(null);
  const [selectedWfhOnsite, setSelectedWfhOnSite] = useState("WFH / On Site");
  const [alamatProject, setAlamatProject] = useState(localStorage.getItem("alamatProject") || "");
  const [loading, setLoading] = useState(false);
  const [valueLokasiWFH, setValueLokasiWFH] = useState("")

  const [valueAbsen, setValueAbsen] = useState("");
  const [position, setPosition] = useState({ latitude: null, longitude: null });

  const [formData, setFormData] = useState({
    projectId: "",
    nik: "",
    nama: "",
    hari: "",
    tgl_absen: null,
    image: "",
  });

  console.log(isTelatMasuk);

  useEffect(() => {
    if ("geolocation" in navigator) {
      navigator.geolocation.getCurrentPosition(function (position) {
        setPosition({
          latitude: position.coords.latitude,
          longitude: position.coords.longitude,
        });
      });
    } else {
      console.log("Geolocation is not available in your browser.");
    }
  }, []);
  const handleProject = (selectedProject) => {
    // Cari data jabatan berdasarkan selected project yaitu nama project
    const selectedItem = projectData.find(
      (jabatan) => jabatan.namaProject === selectedProject
    );

    setDropdownProjectTitle(selectedProject);
    // kalau sudah ketemu set project value nya itu adalah projectId, untuk namaProject berlaku diatas, hanya untuk menampilkan namaProject jika user klik diantara project, kalau ke database kita pakai projectId untuk mengirim nya.
    setProject(selectedItem.projectId);
    console.log(selectedItem);

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
    } else if (selectedWfhOnsite === "WFH"){
      setValueAbsen("");
    } else {
      setValueAbsen(alamatProject);
      console.log("MASUK ON SITE");
    }
  };

  const handleChange = (event) => {
    setValueLokasiWFH(event.target.value);
    console.log(valueLokasiWFH);
  };
  

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
          const filteredData = data.data.filter(item => item.isActive === "1");
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
  }, [navigate, project, isToken]);

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
    handleClose(true)

    e.preventDefault();
    const token = localStorage.getItem("authToken");
    setLoading(true);

    if (!token) {
      console.error("Token is not available");
      return navigate("/login");
    }

    try {
      const requestData = {
        project_id: formData.projectId,
        nik: formData.nik,
        nama: formData.nama,
        hari: formData.hari,
        tgl_absen: formData.tglAbsen,
        lokasi_msk: formData.lokasiMsk,
        jam_msk: formData.jam_msk,
      };

      const response = await axios.post(
        "http://localhost:8081/api/master-data/announcement-form/add",
        requestData,
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        }
      );
      Swal.fire({
        title: "Success!",
        text: "Absen Submitted.",
        icon: "success",
      });
      navigate("/master-data/announcement-view");
      console.log("Response from API:", response.data);
    } catch (error) {
      // Jika tidak berhasil, tampilkan pesan error
      Swal.fire({
        title: "Error!",
        text: "Failed to Absen.",
        icon: "error",
      });
    }
  }

  return (
    <Modal show={show} onHide={handleClose}>
      <Modal.Header closeButton>
        <Modal.Title>{title}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form>
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

          <FormControl disabled={selectedWfhOnsite === "WFH" ? false : true} type="text" placeholder="Lokasi Absen" value={ selectedWfhOnsite === "WFH" ? valueLokasiWFH : valueAbsen } onChange={handleChange} />
          
          {isTelatMasuk && (
            <Form.Group
            className="mb-3"
            controlId="exampleForm.ControlTextarea1"
          >
            <Form.Label style={{color: 'black'}}>Catatan telat masuk</Form.Label>
            <Form.Control as="textarea" rows={3} />
          </Form.Group>
          )}

          {selectedWfhOnsite === "WFH" && (
            <Form.Group className="upload" controlId="formFile">
              <div className="form__row__left">
                <Form.Label>{isTelatMasuk ? "Foto Telat Masuk" : "Foto WFH"}</Form.Label>
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
