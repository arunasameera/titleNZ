import React, { useState } from "react";
import { Button, Col, Form, Input, Row } from "reactstrap";
import { useNavigate } from "react-router";

export default function TitleSearch() {
  const [value, setValue] = useState("");
  const [isValid, setIsValid] = useState(true);
  const navigate = useNavigate();

  function handleChange(e: any) {
    const re = /^[0-9\b]+$/; // Regular expression for numbers only
    if (e.target.value === "" || re.test(e.target.value)) {
      setValue(e.target.value);
      setIsValid(true);
    } else {
      setIsValid(false);
    }
  }

  function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    const sanitizedValue = encodeURIComponent(value);
    navigate(`/titles/${sanitizedValue}`);
  }

  return (
    <div style={{ display: "inline-block" }}>
      <Form onSubmit={handleSubmit}>
        <Row className="row-cols-lg-auto g-3 align-items-center">
          <Col>
            <Input
              type="text"
              value={value}
              onChange={handleChange}
              placeholder="Enter a title number"
              invalid={!isValid}
            />
          </Col>
          <Col>
            <Button
              color="primary"
              type="submit"
              value="Submit"
              disabled={value ? false : true}
            >
              Go
            </Button>
          </Col>
        </Row>
      </Form>
    </div>
  );
}
