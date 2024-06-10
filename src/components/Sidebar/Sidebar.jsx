/* eslint-disable react/prop-types */
/* eslint-disable no-unused-vars */
import "./sidebar.css";
import Logo from "../../images/logo-treemas.png";
import { Link, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import DropdownMenu from "./DropdownMenu/DropdownMenu";
import axios from "axios";

const Sidebar = ({ children }) => {
  const [isSidebarOpen, setIsSidebarOpen] = useState(true);
  const navigate = useNavigate();
  let role

  const handleDropdownToggle = (itemName) => {
    setIsParent((prevState) => ({
      ...Object.keys(prevState).reduce((acc, key) => {
        acc[key] = key === itemName ? !prevState[key] : false;
        return acc;
      }, {}),
    }));
    

    setItemsState((prevState) => ({
      ...Object.keys(prevState).reduce((acc, key) => {
        acc[key] = key === itemName ? !prevState[key] : {};
        return acc;
      }, {}),
    }));
  };


    role = localStorage.getItem("role")


  const handleLogout = async (e) => {
    const token = localStorage.getItem("authToken");

    if (!token) {
      console.error("Token is not available");
      return navigate("/login");
    }

    // Hapus token dari localStorage atau melakukan tindakan logout yang diperlukan

    const response = await fetch(`http://localhost:8081/api/auth/logout`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    console.log(response);
    localStorage.removeItem("authToken");
    localStorage.removeItem("karyawanImg");
    localStorage.removeItem("userName");
    // Redirect ke halaman login setelah logout
    navigate("/login");
  };

  const [itemsState, setItemsState] = useState({
    detailData: {
      //
      absen: false,
      cutiSakit: false,
      reimburse: false,
      timesheet: false,
      tracking: false,
    },
    //
    management: {
      user: false,
      // userAccess: false,
      userMember: false,
    },
    //
    masterData: {
      announcement: false,
      claim: false,
      cuti: false,
      jabatan: false,
      karyawan: false,
      libur: false,
      permission: false,
      project: false,
    },
    //
    parameter: {
      general: false,
      reimburseP: false,
    },
    //

    //
    upload: {
      absenU: false,
      absenApp: false,
      penempatan: false,
    },
  });

  const [isParent, setIsParent] = useState({
    detailData: false,
    management: false,
    // manualService: false,
    masterData: false,
    parameter: false,
    reportData: false,
    upload: false,
    logout: false,
  });

  const handleDropdown = (itemName) => {
    setIsParent((prevState) => {
      const updatedState = { ...prevState };

      // Matikan semua parent kategori selain yang diklik
      for (const key in updatedState) {
        if (key !== itemName) {
          updatedState[key] = false;
        }
      }

      // Aktifkan atau nonaktifkan parent yang diklik
      updatedState[itemName] = !prevState[itemName];

      // Atur nilai anak-anak untuk semua objek menjadi false
      const updatedItemsState = { ...itemsState };
      for (const key in updatedItemsState) {
        updatedItemsState[key] = {};
        for (const childKey in updatedItemsState[key]) {
          updatedItemsState[key][childKey] = false;
        }
      }
      setItemsState(updatedItemsState);

      return updatedState;
    });
  };

  const handleClick = (sectionName, itemName) => {
    setItemsState((prevState) => {
      const newState = { ...prevState };

      // Close all dropdowns under "detailData"
      if (sectionName === "detailData") {
        for (const key in newState.detailData) {
          newState.detailData[key] = false;
        }
      } else if (sectionName === "management") {
        for (const key in newState.management) {
          newState.management[key] = false;
        }
      } else if (sectionName === "manualService") {
        for (const key in newState.manualService) {
          newState.manualService[key] = false;
        }
      } else if (sectionName === "masterData") {
        for (const key in newState.masterData) {
          newState.masterData[key] = false;
        }
      } else if (sectionName === "parameter") {
        for (const key in newState.parameter) {
          newState.parameter[key] = false;
        }
      } else if (sectionName === "reportData") {
        for (const key in newState.reportData) {
          newState.reportData[key] = false;
        }
      } else if (sectionName === "upload") {
        for (const key in newState.upload) {
          newState.upload[key] = false;
        }
      } else if (sectionName === "logout") {
        for (const key in newState.logout) {
          newState.logout[key] = false;
        }
      }

      // Toggle the clicked section
      newState[sectionName][itemName] = !prevState[sectionName][itemName];

      return newState;
    });
  };

  const handleSidebarToggle = () => {
    setIsSidebarOpen(!isSidebarOpen);
  };

  console.log("roled " +role);


  return (
    <div className={`parent__container`}>
      <div
        className={`${
          isSidebarOpen ? "sidebar__container" : "sidebar__container__close"
        }`}
      >
        <button
          className={`${
            isSidebarOpen ? "hamburger-button" : "hamburger-button__close"
          }`}
          onClick={handleSidebarToggle}
        >
          ☰ {/* You can replace this with your hamburger icon */}
        </button>

        <div className={`${isSidebarOpen ? "logo__sidebar" : "logo__close"}`}>
          <Link to="/dashboard">
            <img src={Logo} alt="Logo Treemas" />
          </Link>
        </div>

        <nav
          className={`${
            isSidebarOpen ? "nav__container" : "nav__container__close"
          }`}
        >
          <ul>
            {role === "HEAD"  && (
              <li
              className={isParent.detailData ? "color" : "non_color"}
              onClick={() => handleDropdown("detailData")}
            >
              <div
                className={isParent.detailData  ? "active" : "non_active"}
              ></div>
              <p>Detail Data</p>
              <i
                className={
                  isParent.detailData
                    ? "fa-solid fa-chevron-up"
                    : "fa-solid fa-chevron-down"
                }
              />
            </li>
            )}
            
            {isParent.detailData && role === "HEAD" && (
              <>
                <DropdownMenu
                  onClick={() => handleClick("detailData", "absen")}
                  link="/detail-data/absen-view"
                  text="Absen"
                  isActive={itemsState.detailData.absen}
                />
                <DropdownMenu
                  onClick={() => handleClick("detailData", "cutiSakit")}
                  link="/detail-data/cutisakit-view"
                  text="Cuti/Sakit"
                  isActive={itemsState.detailData.cutiSakit}
                />
                <DropdownMenu
                  onClick={() => handleClick("detailData", "reimburse")}
                  link="/detail-data/reimburse-view"
                  text="Reimburse"
                  isActive={itemsState.detailData.reimburse}
                />
                <DropdownMenu
                  onClick={() => handleClick("detailData", "timesheet")}
                  link="/detail-data/timesheet-view"
                  text="Timesheet"
                  isActive={itemsState.detailData.timesheet}
                />
                <DropdownMenu
                  onClick={() => handleClick("detailData", "tracking")}
                  link="/detail-data/tracking-view"
                  text="Tracking"
                  isActive={itemsState.detailData.tracking}
                />
              </>
            )}

            {role === "HEAD" && (
              <li
              className={isParent.management ? "color" : "non_color"}
              onClick={() => handleDropdown("management")}
            >
              <div
                className={isParent.management ? "active" : "non_active"}
              ></div>
              <p>Management</p>
              <i
                className={
                  isParent.management
                    ? "fa-solid fa-chevron-up"
                    : "fa-solid fa-chevron-down"
                }
              />
            </li>
            )}
            
            {isParent.management && role === "HEAD" && (
              <>
                <DropdownMenu
                  onClick={() => handleClick("management", "user")}
                  link="/management/user-view"
                  text="User"
                  isActive={itemsState.management.user}
                />
                {/* <DropdownMenu
                  onClick={() => handleClick("management", "userAccess")}
                  link="/management/user-access-view"
                  text="User Access"
                  isActive={itemsState.management.userAccess}
                /> */}
                <DropdownMenu
                  onClick={() => handleClick("management", "userMember")}
                  link="/management/user-member-view"
                  text="User Member"
                  isActive={itemsState.management.userMember}
                />
              </>
            )}

            {/* <DropdownMenu
              onClick={() => handleDropdown("manualService")}
              link="/manual-service"
              text="Manual Service"
              isActive={isParent.manualService}
            /> */}
            {role === "HEAD" && (
              <li
              className={isParent.masterData ? "color" : "non_color"}
              onClick={() => handleDropdown("masterData")}
            >
              <div
                className={isParent.masterData ? "active" : "non_active"}
              ></div>
              <p>Master Data</p>
              <i
                className={
                  isParent.masterData
                    ? "fa-solid fa-chevron-up"
                    : "fa-solid fa-chevron-down"
                }
              />
            </li>
            )} 
            
            {isParent.masterData && role === "HEAD" && (
              <>
                <DropdownMenu
                  onClick={() => handleClick("masterData", "announcement")}
                  link="/master-data/announcement-view"
                  text="Announcement"
                  isActive={itemsState.masterData.announcement}
                />
                <DropdownMenu
                  onClick={() => handleClick("masterData", "claim")}
                  link="/master-data/claim-view"
                  text="Claim"
                  isActive={itemsState.masterData.claim}
                />
                <DropdownMenu
                  onClick={() => handleClick("masterData", "cuti")}
                  link="/master-data/cuti-view"
                  text="Cuti"
                  isActive={itemsState.masterData.cuti}
                />
                <DropdownMenu
                  onClick={() => handleClick("masterData", "jabatan")}
                  link="/master-data/jabatan-view"
                  text="Jabatan"
                  isActive={itemsState.masterData.jabatan}
                />
                <DropdownMenu
                  onClick={() => handleClick("masterData", "karyawan")}
                  link="/master-data/karyawan-view"
                  text="Karyawan"
                  isActive={itemsState.masterData.karyawan}
                />
                <DropdownMenu
                  onClick={() => handleClick("masterData", "libur")}
                  link="/master-data/libur-view"
                  text="Libur"
                  isActive={itemsState.masterData.libur}
                />
                <DropdownMenu
                  onClick={() => handleClick("masterData", "permission")}
                  link="/master-data/permission-view"
                  text="Permission"
                  isActive={itemsState.masterData.permission}
                />
                <DropdownMenu
                  onClick={() => handleClick("masterData", "project")}
                  link="/master-data/project-view"
                  text="Project"
                  isActive={itemsState.masterData.project}
                />
              </>
            )}
            {role === "HEAD" && (
              <li
              className={isParent.parameter ? "color" : "non_color"}
              onClick={() => handleDropdown("parameter")}
            >
              <div
                className={isParent.parameter ? "active" : "non_active"}
              ></div>
              <p>Parameter</p>
              <i
                className={
                  isParent.parameter
                    ? "fa-solid fa-chevron-up"
                    : "fa-solid fa-chevron-down"
                }
              />
            </li>
            )}
            
            {isParent.parameter && role === "HEAD" && (
              <>
                <DropdownMenu
                  onClick={() => handleClick("parameter", "general")}
                  link="/parameter/general-view"
                  text="General"
                  isActive={itemsState.parameter.general}
                />
                <DropdownMenu
                  onClick={() => handleClick("parameter", "reimburseP")}
                  link="/parameter/reimburse-view"
                  text="Reimburse"
                  isActive={itemsState.parameter.reimburseP}
                />
              </>
            )}

            {/* <li
              className={isParent.reportData ? "color" : "non_color"}
              onClick={() => handleDropdown("reportData")}
            >
              <div
                className={isParent.reportData ? "active" : "non_active"}
              ></div>
              <p>Report Data</p>
              <i
                className={
                  isParent.reportData
                    ? "fa-solid fa-chevron-up"
                    : "fa-solid fa-chevron-down"
                }
              />
            </li>
            {isParent.reportData && (
              <>
                <DropdownMenu
                  onClick={() => handleClick("reportData", "claimR")}
                  link="/report-data/claim"
                  text="Claim"
                  isActive={itemsState.reportData.claimR}
                />
                <DropdownMenu
                  onClick={() => handleClick("reportData", "detail")}
                  link="/report-data/detail"
                  text="Detail"
                  isActive={itemsState.reportData.detail}
                />
                <DropdownMenu
                  onClick={() => handleClick("reportData", "summary")}
                  link="/report-data/summary"
                  text="Summary"
                  isActive={itemsState.reportData.summary}
                />
              </>
            )} */}

            <li
              className={isParent.upload ? "color" : "non_color"}
              onClick={() => handleDropdown("upload")}
            >
              <div className={isParent.upload ? "active" : "non_active"}></div>
              <p>Upload</p>
              <i
                className={
                  isParent.upload
                    ? "fa-solid fa-chevron-up"
                    : "fa-solid fa-chevron-down"
                }
              />
            </li>
            {isParent.upload && (
              <>
                <DropdownMenu
                  onClick={() => handleClick("upload", "absenU")}
                  link="/upload/absen"
                  text="Absen"
                  isActive={itemsState.upload.absenU}
                />
                {
                  role === "HEAD" && (
                <DropdownMenu
                  onClick={() => handleClick("upload", "absenApp")}
                  link="/upload/absen-approval"
                  text="Absen Approval"
                  isActive={itemsState.upload.absenApp}
                />
                  )
                }
                
                <DropdownMenu
                  onClick={() => handleClick("upload", "penempatan")}
                  link="/upload/penempatan"
                  text="Penempatan"
                  isActive={itemsState.upload.penempatan}
                />
              </>
            )}

            <div className="line"></div>
            <DropdownMenu
              onClick={handleLogout}
              text="Logout"
              isActive={itemsState.logout}
            />
          </ul>
        </nav>
      </div>
      <main
        className={`${
          isSidebarOpen ? "main__container" : "main__container__close"
        }`}
      >
        {children}
      </main>
    </div>
  );
};

export default Sidebar;
