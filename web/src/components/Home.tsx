import React from "react";
import TitleSearch from "./TitleSearch";

export default function Home() {
  return (
    <div style={{ position: "relative" }}>
      <div className="home-css">
        <h3 className="green-text">Welcome to LandOnLite</h3>
        <p>You can enter a title number (e.g. "1") to view it.</p>
        <TitleSearch />
      </div>
    </div>
  );
}
