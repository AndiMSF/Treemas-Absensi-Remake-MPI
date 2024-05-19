import "./formAbsen.css";
import { useEffect, useState } from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';
import DropdownMenu from "../../Elements/DropdownMenu/DropdownMenu";
import { Dropdown, FormControl } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const FormAbsen = ({ show, handleClose, title }) => {
  const navigate = useNavigate();
  const [dropdownProjectTitle, setDropdownProjectTitle] = useState('Pilih Project');
  const [projectData, setProjectData] = useState([]);
  const [project, setProject] = useState("");
  const [isToken, setIstoken] = useState("");
  const [error, setError] = useState(null);
  const [selectedWfhOnsite, setSelectedWfhOnSite] = useState("WFH / On Site");
  const [alamatProject, setAlamatProject] = useState(localStorage.getItem("alamatProject") || "");

  const [valueAbsen, setValueAbsen] = useState("");
  const [position, setPosition] = useState({ latitude: null, longitude: null });

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
      setValueAbsen("SAWANGAN");
      console.log(position);
    } else {
      setValueAbsen(alamatProject);
      console.log("MASUK ON SITE");
    }
  };

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
          setProjectData(data.data);
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

          <FormControl disabled={true} type="text" placeholder="Lokasi Absen" value={ selectedWfhOnsite === "WFH" ? "SAWANGAN" : valueAbsen } />

          <Form.Group
            className="mb-3"
            controlId="exampleForm.ControlTextarea1"
          >
            <Form.Label>Example textarea</Form.Label>
            <Form.Control as="textarea" rows={3} />
          </Form.Group>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        <Button variant="primary" onClick={handleClose}>
          Save Changes
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default FormAbsen;
