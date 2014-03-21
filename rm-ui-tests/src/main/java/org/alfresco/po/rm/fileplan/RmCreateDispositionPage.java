package org.alfresco.po.rm.fileplan;

import org.alfresco.po.share.site.document.FolderDetailsPage;
import org.alfresco.webdrone.RenderTime;
import org.alfresco.webdrone.WebDrone;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.alfresco.webdrone.RenderElement.getVisibleRenderElement;

/**
 * Records management Create Disposition page.
 *
 * @author Polina Lushchinskaya
 * @version 1.1
 */
public class RmCreateDispositionPage extends FolderDetailsPage {

    public static By CREATE_DISPOSITION_BUTTON  = By.cssSelector("button[id$='createschedule-button-button']");
    public static By DISPOSITION_SECTION        = By.cssSelector("div[class$='disposition']");
    public static By EDIT_PROPERTIES_BUTTON     = By.cssSelector("button[id$='editproperties-button-button']");
    public static By EDIT_SCHEDULE_BUTTON       = By.cssSelector("button[id$='editschedule-button-button']");

    public static enum DispositionAction{
        ACCESSION(0, By.xpath("//a[text()='Accession']"), "Accession", "accession"),
        DESTROY(2,  By.xpath("//a[text()='Destroy']"), "Destroy", "destroy"),
        RETAIN(3,  By.xpath("//a[text()='Retain']"), "Retain", "retain"),
        TRANSFER(4,  By.xpath("//a[text()='Transfer']"), "Transfer", "transfer"),
        CUTOFF(1,  By.xpath("//a[text()='Cut off']"), "Cut off", "cutoff");

        public final int numberPosition;
        private final By xpath;
        private final String name;
        private final String value;

        DispositionAction(int numberPosition, By xpath, String name, String value)
        {
            this.numberPosition = numberPosition;
            this.xpath = xpath;
            this.name = name;
            this.value = value;
        }

        public By getXpath()
        {
            return xpath;
        }

        public String getName()
        {
            return name;
        }

        public String getValue()
        {
            return value;
        }
    }

    /**
     * Constructor
     *
     * @param drone
     */
    public RmCreateDispositionPage(WebDrone drone) {
        super(drone);
    }

    @Override
    public RmCreateDispositionPage render(RenderTime timer)
    {
        elementRender(timer,
                getVisibleRenderElement(DISPOSITION_SECTION),
                getVisibleRenderElement(EDIT_PROPERTIES_BUTTON),
                getVisibleRenderElement(EDIT_SCHEDULE_BUTTON));
        return this;
    }

    @Override
    public RmCreateDispositionPage render()
    {
        return render(new RenderTime(maxPageLoadingTime));
    }

    @Override
    public RmCreateDispositionPage render(final long time)
    {
        return render(new RenderTime(time));
    }

    /**
     * Helper method that clicks by element
     *
     * @param locator element By locator
     */

    public void click(By locator)
    {
        WebElement element = drone.findAndWait(locator);
        drone.mouseOverOnElement(element);
        element.click();
    }

    public RmEditDispositionSchedulePage selectEditDisposition(){
        click(EDIT_SCHEDULE_BUTTON);
        return new RmEditDispositionSchedulePage(drone).render();

    }
}