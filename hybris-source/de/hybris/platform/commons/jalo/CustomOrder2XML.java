package de.hybris.platform.commons.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.price.DiscountInformation;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.util.Utilities;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.commons.lang.StringUtils;

public class CustomOrder2XML extends GeneratedCustomOrder2XML
{
    private static final String LINEBREAK = System.getProperty("line.separator");
    private static final String TAB1 = "\t";
    private static final String TAB2 = "\t\t";
    private static final String TAB3 = "\t\t\t";
    private static final String TAB4 = "\t\t\t\t";


    public Media format(Item item) throws JaloBusinessException
    {
        if(item != null && !(item instanceof AbstractOrder))
        {
            return null;
        }
        AbstractOrder ao = (AbstractOrder)item;
        File outputFile = null;
        try
        {
            outputFile = File.createTempFile("returnmediatempfile", ".xml");
            FileWriter fw = new FileWriter(outputFile);
            writeXMLHeader(fw, ao);
            writeSender(fw, ao);
            writeResponsibleEmployee(fw, ao);
            writeCustomerInfo(fw, ao);
            writeQuotationEntries(fw, ao);
            writeClosingingQuotationTag(fw);
            fw.close();
            Media returnMedia = MediaManager.getInstance().createMedia("Order2FO-" + System.currentTimeMillis());
            returnMedia.setFile(outputFile);
            return returnMedia;
        }
        catch(IOException e)
        {
            throw new JaloSystemException(e);
        }
        finally
        {
            try
            {
                if(outputFile != null && outputFile.exists())
                {
                    outputFile.delete();
                }
            }
            catch(Exception exception)
            {
            }
        }
    }


    private void writeXMLHeader(FileWriter fw, AbstractOrder ao) throws IOException
    {
        SimpleDateFormat dateFormat = Utilities.getSimpleDateFormat("dd.mm.yyyy");
        fw.write("<?xml version=\"1.0\" encoding=\"" + Config.getString("UTF-8", "ISO-8859-1") + "\" ?>" + LINEBREAK);
        fw.write("<QUOTATION TITLE=\"" + Utilities.escapeHTML("testtitel") + "\" GENERATIONDATE=\"" +
                        Utilities.escapeHTML(dateFormat.format(ao.getDate())) + "\">" + LINEBREAK);
    }


    private void writeSender(FileWriter fw, AbstractOrder ao) throws JaloBusinessException, IOException
    {
        Address companyAddress = (ao.getPaymentAddress() != null) ? ao.getPaymentAddress() : ao.getDeliveryAddress();
        if(companyAddress == null)
        {
            throw new JaloBusinessException("No company address specified.");
        }
        Country companyCountry = companyAddress.getCountry();
        if(companyCountry == null)
        {
            throw new JaloBusinessException("No company country specified.");
        }
        String companyname = (companyAddress.getAttribute("company") == null) ? "" : (String)companyAddress.getAttribute("company");
        String streetname = (companyAddress.getAttribute("streetname") == null) ? "" : (String)companyAddress.getAttribute("streetname");
        String streetnumber = (companyAddress.getAttribute("streetnumber") == null) ? "" : (String)companyAddress.getAttribute("streetnumber");
        String street = streetname + " " + streetname;
        String pob = (companyAddress.getAttribute("pobox") == null) ? "" : (String)companyAddress.getAttribute("pobox");
        String zipcode = (companyAddress.getAttribute("postalcode") == null) ? "" : (String)companyAddress.getAttribute("postalcode");
        String city = (companyAddress.getAttribute("town") == null) ? "" : (String)companyAddress.getAttribute("town");
        String country = (companyCountry.getName() == null) ? "" : companyCountry.getName();
        String combinedData =
                        (companyname.trim().isEmpty() ? companyname : (companyname + companyname)) + (companyname.trim().isEmpty() ? companyname : (companyname + companyname)) + (street.trim().isEmpty() ? street : (street + street)) + (pob.trim().isEmpty() ? pob : (pob + pob)) + " " + zipcode + (
                                        city.trim().isEmpty()
                                                        ? city
                                                        : (city + city));
        fw.write("\t<SENDER>" + LINEBREAK);
        fw.write("\t\t<COMPANY_NAME>" + Utilities.escapeHTML(companyname) + "</COMPANY_NAME>" + LINEBREAK);
        fw.write("\t\t<STREET>" + Utilities.escapeHTML(street) + "</STREET>" + LINEBREAK);
        fw.write("\t\t<POB>" + Utilities.escapeHTML(pob) + "</POB>" + LINEBREAK);
        fw.write("\t\t<ZIPCODE>" + Utilities.escapeHTML(zipcode) + "</ZIPCODE>" + LINEBREAK);
        fw.write("\t\t<CITY>" + Utilities.escapeHTML(city) + "</CITY>" + LINEBREAK);
        fw.write("\t\t<COUNTRY>" + Utilities.escapeHTML(country) + "</COUNTRY>" + LINEBREAK);
        fw.write("\t\t<COMBINED_DATA>" + Utilities.escapeHTML(combinedData) + "</COMBINED_DATA>" + LINEBREAK);
        fw.write("\t</SENDER>" + LINEBREAK);
    }


    private void writeResponsibleEmployee(FileWriter fw, AbstractOrder ao) throws JaloBusinessException, IOException
    {
        User responsibleEmployee = ao.getUser();
        if(responsibleEmployee == null)
        {
            throw new JaloBusinessException("No responsible employee defined for quotation.");
        }
        Address employeeAddress = responsibleEmployee.getDefaultPaymentAddress();
        if(employeeAddress == null)
        {
            throw new JaloBusinessException("No address specified for responsible employee [" + responsibleEmployee
                            .getName() + "].");
        }
        String prename = (employeeAddress.getAttribute("firstname") == null) ? "" : (String)employeeAddress.getAttribute("firstname");
        String lastname = (employeeAddress.getAttribute("lastname") == null) ? "" : (String)employeeAddress.getAttribute("lastname");
        String dept = (employeeAddress.getAttribute("department") == null) ? "" : (String)employeeAddress.getAttribute("department");
        String occupation = "Key Account Manager";
        String tel = (employeeAddress.getAttribute("phone1") == null) ? "" : (String)employeeAddress.getAttribute("phone1");
        String mobil = (employeeAddress.getAttribute("cellphone") == null) ? "" : (String)employeeAddress.getAttribute("cellphone");
        String fax = (employeeAddress.getAttribute("fax") == null) ? "" : (String)employeeAddress.getAttribute("fax");
        String email = (employeeAddress.getAttribute("email") == null) ? "" : (String)employeeAddress.getAttribute("email");
        String combinedData = prename + " " + prename + (StringUtils.isBlank(lastname) ? lastname : (lastname + lastname)) + (StringUtils.isBlank(dept) ? dept : (dept + dept)) + (StringUtils.isBlank("Key Account Manager") ? "Key Account Manager" : ("Key Account Manager" + LINEBREAK))
                        + (StringUtils.isBlank(tel) ? tel : (tel + tel)) + (StringUtils.isBlank(mobil) ? mobil : (mobil + mobil)) + (StringUtils.isBlank(fax) ? fax : (fax + fax));
        fw.write("\t<RESPONSIBLE_USER>" + LINEBREAK);
        fw.write("\t\t<PRENAME>" + Utilities.escapeHTML(prename) + "</PRENAME>" + LINEBREAK);
        fw.write("\t\t<LASTNAME>" + Utilities.escapeHTML(lastname) + "</LASTNAME>" + LINEBREAK);
        fw.write("\t\t<TEL>" + Utilities.escapeHTML(tel) + "</TEL>" + LINEBREAK);
        fw.write("\t\t<MOBIL>" + Utilities.escapeHTML(mobil) + "</MOBIL>" + LINEBREAK);
        fw.write("\t\t<FAX>" + Utilities.escapeHTML(fax) + "</FAX>" + LINEBREAK);
        fw.write("\t\t<EMAIL>" + Utilities.escapeHTML(email) + "</EMAIL>" + LINEBREAK);
        fw.write("\t\t<DEPT>" + Utilities.escapeHTML(dept) + "</DEPT>" + LINEBREAK);
        fw.write("\t\t<OCCUPATION>" + Utilities.escapeHTML("Key Account Manager") + "</OCCUPATION>" + LINEBREAK);
        fw.write("\t\t<COMBINED_DATA>" + Utilities.escapeHTML(combinedData) + "</COMBINED_DATA>" + LINEBREAK);
        fw.write("\t</RESPONSIBLE_USER>" + LINEBREAK);
    }


    private void writeCustomerInfo(FileWriter fw, AbstractOrder ao) throws JaloBusinessException, IOException
    {
        SimpleDateFormat dateFormat = Utilities.getSimpleDateFormat("dd.mm.yyyy");
        Calendar calendar = Utilities.getDefaultCalendar();
        User customer = ao.getUser();
        if(customer == null)
        {
            throw new JaloBusinessException("No receiping Customer set for quotation.");
        }
        String customerid = (customer.getUID() == null) ? "" : customer.getUID();
        String quotationnr = (ao.getCode() == null) ? "" : ao.getCode();
        fw.write("\t<CUSTOMER_INFO>" + LINEBREAK);
        fw.write("\t\t<CUSTOMER_ID>" + Utilities.escapeHTML(customerid) + "</CUSTOMER_ID>" + LINEBREAK);
        fw.write("\t\t<QUOTATION_NR>" + Utilities.escapeHTML(quotationnr) + "</QUOTATION_NR>" + LINEBREAK);
        calendar.set(2017, 5, 11);
        fw.write("\t\t<QUOTATION_PRICE_EXPIRATION_DATE>" + Utilities.escapeHTML(dateFormat.format(calendar.getTime())) + "</QUOTATION_PRICE_EXPIRATION_DATE>" + LINEBREAK);
        fw.write("\t\t<QUOTATION_CONTENT_EXPIRATION_DATE>" + Utilities.escapeHTML(dateFormat.format(calendar.getTime())) + "</QUOTATION_CONTENT_EXPIRATION_DATE>" + LINEBREAK);
        fw.write("\t</CUSTOMER_INFO>" + LINEBREAK);
    }


    private void writeQuotationEntries(FileWriter fw, AbstractOrder ao) throws JaloBusinessException, IOException
    {
        Collection quotationEntries = ao.getEntries(0, 2147483647);
        if(quotationEntries.isEmpty())
        {
            throw new JaloBusinessException("No quotation entries specified.");
        }
        PriceValue priceVal = null;
        DiscountValue discountVal = null;
        String position = "";
        String pieces = "";
        String unit = "";
        String materialnr = "";
        String materialdescription = "";
        String productname = "";
        String listprice = "";
        String netprice = "";
        String discount = "";
        String deliverytime = "";
        String listpricessum = "--";
        String totaldiscount = String.valueOf(ao.getTotalDiscounts());
        String netpricessum = "--";
        String totalprice = String.valueOf(ao.getTotal());
        fw.write("\t\t<QUOTATION_ENTRIES>" + LINEBREAK);
        Collection<AbstractOrderEntry> sortedQuotationEntries = new LinkedList();
        AbstractOrderEntry[] sortedEntries = new AbstractOrderEntry[quotationEntries.size()];
        for(Iterator<AbstractOrderEntry> iterator1 = quotationEntries.iterator(); iterator1.hasNext(); )
        {
            AbstractOrderEntry currentEntry = iterator1.next();
            sortedEntries[currentEntry.getEntryNumber().intValue()] = currentEntry;
        }
        for(int i = 0; i < sortedEntries.length; i++)
        {
            sortedQuotationEntries.add(sortedEntries[i]);
        }
        for(Iterator<AbstractOrderEntry> quotationEntriesIter = sortedQuotationEntries.iterator(); quotationEntriesIter.hasNext(); )
        {
            AbstractOrderEntry currentEntry = quotationEntriesIter.next();
            Unit entryUnit = currentEntry.getUnit();
            if(entryUnit == null)
            {
                throw new JaloBusinessException("No unit specified for quotation entry.");
            }
            Product entryProduct = currentEntry.getProduct();
            if(entryProduct == null)
            {
                throw new JaloBusinessException("No product specified for quotation entry.");
            }
            Collection<PriceInformation> priceInfos = entryProduct.getPriceInformations(true);
            if(!priceInfos.isEmpty())
            {
                PriceInformation priceInfo = priceInfos.iterator().next();
                priceVal = priceInfo.getPriceValue();
            }
            Collection<DiscountInformation> discountInfos = entryProduct.getDiscountInformations(true);
            if(!discountInfos.isEmpty())
            {
                DiscountInformation discountInfo = discountInfos.iterator().next();
                discountVal = discountInfo.getDiscountValue();
            }
            position = String.valueOf(currentEntry.getEntryNumber().intValue() + 1);
            pieces = String.valueOf(currentEntry.getQuantity());
            unit = (entryUnit.getName() == null) ? "" : entryUnit.getName();
            materialnr = (entryProduct.getCode() == null) ? "" : entryProduct.getCode();
            materialdescription = (entryProduct.getDescription() == null) ? "" : entryProduct.getDescription();
            productname = (entryProduct.getName() == null) ? "" : entryProduct.getName();
            listprice = String.valueOf((priceVal != null) ? priceVal.getValue() : -1.0D);
            netprice = String.valueOf((priceVal != null) ? priceVal.getValue() : -1.0D);
            discount = (discountVal == null) ? "" : String.valueOf(discountVal.getValue());
            deliverytime = "14 days";
            fw.write("\t\t\t<QUOTATION_ENTRY>" + LINEBREAK);
            fw.write("\t\t\t\t<POSITION>" + Utilities.escapeHTML(position) + "</POSITION>" + LINEBREAK);
            fw.write("\t\t\t\t<PIECES>" + Utilities.escapeHTML(pieces) + "</PIECES>" + LINEBREAK);
            fw.write("\t\t\t\t<UNIT>" + Utilities.escapeHTML(unit) + "</UNIT>" + LINEBREAK);
            fw.write("\t\t\t\t<MATERIAL_NR>" + Utilities.escapeHTML(materialnr) + "</MATERIAL_NR>" + LINEBREAK);
            fw.write("\t\t\t\t<MATERIAL_DESCRIPTION>" + Utilities.escapeHTML(materialdescription) + "</MATERIAL_DESCRIPTION>" + LINEBREAK);
            fw.write("\t\t\t\t<PRODUCT_NAME>" + Utilities.escapeHTML(productname) + "</PRODUCT_NAME>" + LINEBREAK);
            fw.write("\t\t\t\t<LISTPRICE>" + Utilities.escapeHTML(listprice) + "</LISTPRICE>" + LINEBREAK);
            fw.write("\t\t\t\t<NET_PRICE>" + Utilities.escapeHTML(netprice) + "</NET_PRICE>" + LINEBREAK);
            fw.write("\t\t\t\t<DISCOUNT>" + Utilities.escapeHTML(discount) + "</DISCOUNT>" + LINEBREAK);
            fw.write("\t\t\t\t<DELIVERY_TIME>" + Utilities.escapeHTML(deliverytime) + "</DELIVERY_TIME>" + LINEBREAK);
            fw.write("\t\t\t</QUOTATION_ENTRY>" + LINEBREAK);
        }
        fw.write("\t\t\t<LIST_PRICES_SUM>" + Utilities.escapeHTML("--") + "</LIST_PRICES_SUM>" + LINEBREAK);
        fw.write("\t\t\t<TOTAL_DISCOUNT>" + Utilities.escapeHTML(totaldiscount) + "</TOTAL_DISCOUNT>" + LINEBREAK);
        fw.write("\t\t\t<NET_PRICES_SUM>" + Utilities.escapeHTML("--") + "</NET_PRICES_SUM>" + LINEBREAK);
        fw.write("\t\t\t<TOTAL_PRICE>" + Utilities.escapeHTML(totalprice) + "</TOTAL_PRICE>" + LINEBREAK);
        fw.write("\t\t</QUOTATION_ENTRIES>" + LINEBREAK);
    }


    private void writeClosingingQuotationTag(FileWriter fw) throws IOException
    {
        fw.write("</QUOTATION>" + LINEBREAK);
    }
}
