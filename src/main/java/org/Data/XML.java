package org.Data;

import org.Logic.OnBoardComputer;

import javax.xml.stream.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class XML {

    public RecordModel read(String path) throws XMLStreamException, FileNotFoundException {
        InputStream is = new FileInputStream(path);
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = null;
        try {
            reader = inputFactory.createXMLStreamReader(is);
            return readDocument(reader);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private RecordModel readDocument(XMLStreamReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    if (elementName.equals("Dashboard"))
                        return readDashboard(reader);
                    break;
                case XMLStreamReader.END_ELEMENT:
                    break;
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

    private RecordModel readDashboard(XMLStreamReader reader) throws XMLStreamException {
        RecordModel record = new RecordModel();

        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    switch (elementName) {
                        case "OnBoardComputer":
                            record.setOnBoardComputer(readOnBoardComputer(reader));
                            break;
                        case "Counter":
                            record.setCounter(readInt(reader));
                            break;
                        case "DailyCounter1":
                            record.setDayCounter1(readFloat(reader));
                            break;
                        case "DailyCounter2":
                            record.setDayCounter2(readFloat(reader));
                            break;
                        case "CreateDate":
                            record.setCreateDate(readCharacters(reader));
                            break;
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return record;
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

    private OnBoardComputer readOnBoardComputer(XMLStreamReader reader) throws XMLStreamException {
        OnBoardComputer onBoardComputer = new OnBoardComputer();
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    switch (elementName) {
                        case "avgSpeed":
                            onBoardComputer.setAvgSpeed(readFloat(reader));
                            break;
                        case "maxSpeed":
                            onBoardComputer.setMaxSpeed(readFloat(reader));
                            break;
                        case "avgFuel":
                            onBoardComputer.setAvgCombustion(readFloat(reader));
                            break;
                        case "maxFuel":
                            onBoardComputer.setMaxCombustion(readFloat(reader));
                            break;
                        case "journeyDistance":
                            onBoardComputer.setJourneyDistance(readFloat(reader));
                            break;
                        case "journeyTime":
                            onBoardComputer.setJourneyTime(readInt(reader));
                            break;
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return onBoardComputer;
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

    private String readCharacters(XMLStreamReader reader) throws XMLStreamException {
        StringBuilder result = new StringBuilder();
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.CHARACTERS:
                case XMLStreamReader.CDATA:
                    result.append(reader.getText());
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return result.toString();
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

    private int readInt(XMLStreamReader reader) throws XMLStreamException {
        String characters = readCharacters(reader);
        try {
            return Integer.parseInt(characters);
        } catch (NumberFormatException e) {
            throw new XMLStreamException("Invalid integer " + characters);
        }
    }

    private float readFloat(XMLStreamReader reader) throws XMLStreamException {
        String characters = readCharacters(reader);
        try {
            return Float.parseFloat(characters);
        } catch (NumberFormatException e) {
            throw new XMLStreamException("Invalid integer " + characters);
        }
    }

    public void write(String path, RecordModel record) throws IOException, XMLStreamException {
        try (OutputStream os = Files.newOutputStream(Paths.get(path))) {
            XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
            XMLStreamWriter writer = null;
            try {
                writer = outputFactory.createXMLStreamWriter(os, "utf-8");
                writeDashboardElem(writer, record);
            } finally {
                if (writer != null)
                    writer.close();
            }
        }
    }

    private void writeDashboardElem(XMLStreamWriter writer, RecordModel record) throws XMLStreamException {
        writer.writeStartDocument("utf-8", "1.0");
        writer.writeCharacters("\n");
        writer.writeStartElement("Dashboard");
        writer.writeCharacters("\n");
        writer.writeCharacters("\t");
            writeOnBoardComputerElem(writer, record);
        writer.writeCharacters("\n");
        writer.writeCharacters("\t");
            writer.writeStartElement("Counter");
                writer.writeCharacters(Integer.toString(record.getCounter()));
            writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeCharacters("\t");
            writer.writeStartElement("DailyCounter1");
                writer.writeCharacters(Float.toString(Math.round(record.getDayCounter1() *100.0f)/100.0f));
            writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeCharacters("\t");
            writer.writeStartElement("DailyCounter2");
                writer.writeCharacters(Float.toString(Math.round(record.getDayCounter2() *100.0f)/100.0f));
            writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeCharacters("\t");
            writer.writeStartElement("CreateDate");
                writer.writeCharacters(record.getCreateDate().toString());
            writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeEndElement();
        writer.writeEndDocument();
    }

    private void writeOnBoardComputerElem(XMLStreamWriter writer, RecordModel record) throws XMLStreamException {
        writer.writeStartElement("OnBoardComputer");
        writer.writeCharacters("\n");
        writer.writeCharacters("\t\t");
        writer.writeStartElement("avgSpeed");
            writer.writeCharacters(Float.toString(record.getAvgSpeed()));
        writer.writeEndElement();

        writer.writeCharacters("\n");
        writer.writeCharacters("\t\t");
        writer.writeStartElement("maxSpeed");
            writer.writeCharacters(Float.toString(record.getMaxSpeed()));
        writer.writeEndElement();

        writer.writeCharacters("\n");
        writer.writeCharacters("\t\t");
        writer.writeStartElement("avgFuel");
            writer.writeCharacters(Float.toString(record.getAvgFuel()));
        writer.writeEndElement();

        writer.writeCharacters("\n");
        writer.writeCharacters("\t\t");
        writer.writeStartElement("maxFuel");
            writer.writeCharacters(Float.toString(record.getMaxFuel()));
        writer.writeEndElement();

        writer.writeCharacters("\n");
        writer.writeCharacters("\t\t");
        writer.writeStartElement("journeyDistance");
            writer.writeCharacters(Float.toString(Math.round(record.getJourneyDistance() *100.0f)/100.0f));
        writer.writeEndElement();

        writer.writeCharacters("\n");
        writer.writeCharacters("\t\t");
        writer.writeStartElement("journeyTime");
            writer.writeCharacters(Integer.toString(record.getJourneyTime()));
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeCharacters("\t");
        writer.writeEndElement();
    }
}
