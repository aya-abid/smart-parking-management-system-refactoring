package com.parking.client.service;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.barcode.BarCode;
import com.github.anastaciocintra.output.PrinterOutputStream;
import com.parking.client.model.ParkingTicket;

import javax.print.PrintService;

/** Prints the parking slip on the thermal receipt printer. */
public class TicketPrinter {

    private static final String PRINTER_NAME = "THERMAL Receipt Printer";

    public void print(ParkingTicket ticket) {
        try {
            PrintService printService = PrinterOutputStream.getPrintServiceByName(PRINTER_NAME);
            EscPos escpos = new EscPos(new PrinterOutputStream(printService));

            Style header = new Style()
                    .setFontSize(Style.FontSize._2, Style.FontSize._2)
                    .setJustification(EscPosConst.Justification.Center)
                    .setBold(true)
                    .setColorMode(Style.ColorMode.WhiteOnBlack);
            Style center = new Style()
                    .setFontName(Style.FontName.Font_A_Default)
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setJustification(EscPosConst.Justification.Center);
            Style left = new Style()
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setJustification(EscPosConst.Justification.Left_Default);
            Style right = new Style()
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setJustification(EscPosConst.Justification.Right);

            escpos.writeLF(header, " Alpha One Parking ");
            escpos.writeLF(center, "SCO-108, Ranjeet Avenue");
            escpos.writeLF(center, "Date Time : " + ticket.getEntryDate() + " " + ticket.getEntryTime());
            escpos.writeLF(center, " ");
            escpos.writeLF(center, "----------------------------------------");

            escpos.write(left, "Parking Bay     : ");
            escpos.writeLF(right, ticket.getParkingBay());
            escpos.write(left, "Vehicle No      : ");
            escpos.writeLF(right, ticket.getVehicleNo());
            escpos.write(left, "Vehicle Type    : ");
            escpos.writeLF(right, ticket.getVehicleType());
            escpos.write(left, "Location        : ");
            escpos.writeLF(right, ticket.getLocation());
            escpos.write(left, "Employee Id     : ");
            escpos.writeLF(right, ticket.getEmployeeId());
            escpos.write(left, "Ticket No.      : ");
            escpos.writeLF(right, ticket.getTicketNo());
            escpos.writeLF(center, " ");

            BarCode barcode = new BarCode();
            barcode.setJustification(EscPosConst.Justification.Center);
            barcode.setHRIPosition(BarCode.BarCodeHRIPosition.BelowBarCode);
            escpos.write(barcode, ticket.getTicketNo());
            escpos.feed(5);
            escpos.cut(EscPos.CutMode.PART);
            escpos.close();
        } catch (Exception e) {
            // no printer connected
            System.out.println(e.getMessage());
        }
    }
}
