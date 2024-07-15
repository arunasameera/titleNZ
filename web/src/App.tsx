import React from "react";
import "./App.css";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Home from "./components/Home";
import TitlePageWrapper from "./components/TitlePageWrapper";

function App() {
  return (
    <div style={{ margin: "15px" }}>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/titles/:titleNo" element={<TitlePageWrapper />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
