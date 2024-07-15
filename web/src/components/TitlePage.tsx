import React, { useEffect, useState } from "react";
import {
  Button,
  Card,
  CardBody,
  Col,
  Form,
  Input,
  Row,
  Table,
} from "reactstrap";
import { useParams } from "react-router";
import {TitleWithHistory } from "./types"; 


export function TitlePage() {
  let { titleNo } = useParams<{ titleNo: string }>();
  const [data, setData] = useState<TitleWithHistory | undefined>(undefined);
  const [ownerChangeValue, setOwnerChangeValue] = useState("");
  const [currentOwner, setCurrentOwner] = useState(data?.title?.ownerName);
  const [isValid, setIsValid] = useState(true);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadTitle();
  }, []);

  function ownerNameHandleChange(event: React.ChangeEvent<HTMLInputElement>) {
    const re = /^[a-zA-Z\s]*$/;
    if (re.test(event.target.value)) {
      setOwnerChangeValue(event.target.value);
      setIsValid(true);
    } else {
      setIsValid(false);
    }
  }

  async function ownerNameHandleSubmit(event: React.FormEvent) {
    event.preventDefault();

    try {
      const res = await fetch(`/api/titles/${titleNo}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ newOwnerName: ownerChangeValue }),
      });
      if (!res.ok) {
        throw new Error("Failed to update owner name");
      }
      const updatedData = await res.json();

      setOwnerChangeValue("");
      setCurrentOwner(updatedData.ownerName);

      setData((prevData) =>
        prevData
          ? {
              ...prevData,
              title: { ...prevData.title, ownerName: updatedData.ownerName },
              ownerNames: [...prevData.ownerNames, updatedData.ownerName],
            }
          : undefined
      );
    } catch (error) {
      console.error("Error:", error);
    }
  }

  async function loadTitle() {
    setLoading(true);
    try {
      const res = await fetch(`/api/titles/${titleNo}`);
      if (res.status === 200) {
        const titleData: TitleWithHistory = await res.json();
        setData(titleData);
        setCurrentOwner(titleData?.title?.ownerName);
        console.log("loadTitle titleData ", titleData);
      } else {
        throw new Error("Failed to fetch title data");
      }
    } catch (error) {
      console.error("Error:", error);
      setError(
        "Failed to load title data for the given value. Please try again."
      );
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="center-data">
      <h3 className="green-text">Title #{titleNo}</h3>
      {loading && <p>Loading...</p>}
      {error && <p className="error-text">{error}</p>}
      {data && (
        <div>
          <Table>
            <tbody>
              <tr>
                <th className="green-text">Description</th>
                <td>{data?.title?.description}</td>
              </tr>
              <tr>
                <th className="green-text">Current Owner</th>
                <td>{currentOwner}</td>
              </tr>
              <tr>
                <th className="green-text">Owner History</th>
                <td>
                  <ul>
                    {data?.ownerNames?.map((owner: string, index: number) => (
                      <li key={index}>{owner}</li>
                    ))}
                  </ul>
                </td>
              </tr>
            </tbody>
          </Table>
          <Card color="light" style={{ marginTop: "50px" }}>
            <CardBody>
              <h4 className="green-text">Change Owner</h4>
              <p>
                As a registered conveyancing lawyer, you may record a change of
                ownership of this title.
              </p>
              <Form inline onSubmit={ownerNameHandleSubmit}>
                <Row className="align-items-center">
                  <Col xs="8">
                    <Input
                      type="text"
                      value={ownerChangeValue}
                      onChange={ownerNameHandleChange}
                      placeholder="Enter the new owner name"
                      invalid={!isValid}
                      className="new-owner-input"
                    />
                  </Col>
                  <Col xs="4">
                    <Button
                      color="primary"
                      disabled={ownerChangeValue ? false : true}
                      type="submit"
                      value="Submit"
                    >
                      Save
                    </Button>
                  </Col>
                </Row>
              </Form>
            </CardBody>
          </Card>
        </div>
      )}
    </div>
  );
}
