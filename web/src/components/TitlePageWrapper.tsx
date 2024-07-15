import React from "react";
import { Link } from "react-router-dom";
import { TitlePage } from "./TitlePage";

export default function TitlePageWrapper() {
  return (
    <div className="xyz">
      <Link style={{ marginRight: "10px" }} to="/">
        &lt; Home
      </Link>
      <TitlePage />
    </div>
  );
}
