package fr.paris.lutece.plugins.newsletter.service.section;

import fr.paris.lutece.plugins.newsletter.business.section.FreeHtmlSection;
import fr.paris.lutece.plugins.newsletter.business.section.FreeHtmlSectionHome;
import fr.paris.lutece.plugins.newsletter.business.section.NewsletterSection;
import fr.paris.lutece.plugins.newsletter.service.NewsletterPlugin;
import fr.paris.lutece.plugins.newsletter.util.NewsletterUtils;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


/**
 * Service to manage sections with free html.
 */
public class FreeHtmlSectionService implements INewsletterSectionService
{

    /**
     * Code of the section type
     */
    public static final String NEWSLETTER_FREE_HTML_SECTION_TYPE_CODE = "FREE_HTML";

    private static final String NEWSLETTER_FREE_HTML_SECTION_TYPE_NAME = "newsletter.section.freeHtmlSectionType";

    // MARKS
    private static final String MARK_HTML_SECTION = "htmlSection";
    private static final String MARK_WEBAPP_URL = "webapp_url";
    private static final String MARK_LOCALE = "locale";

    // PARAMETERS
    private static final String PARAMETER_CONTENT = "html_content";

    // TEMPLATES
    private static final String TEMPLATE_MODIFY_FREE_HTML_CONFIGURATION = "admin/plugins/newsletter/free_html/modify_config_free_html.html";

    private Plugin _plugin;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getNewsletterSectionTypeCode( )
    {
        return NEWSLETTER_FREE_HTML_SECTION_TYPE_CODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNewsletterSectionTypeName( Locale locale )
    {
        return I18nService.getLocalizedString( NEWSLETTER_FREE_HTML_SECTION_TYPE_NAME, locale );
    }

    /**
     * {@inheritDoc}
     * @return This method always returns <i>true</i>.
     */
    public boolean hasConfiguration( )
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfigurationPage( NewsletterSection newsletterSection, String strBaseUrl, AdminUser user,
            Locale locale )
    {
        FreeHtmlSection htmlSection = FreeHtmlSectionHome.findByPrimaryKey( newsletterSection.getId( ), getPlugin( ) );
        Map<String, Object> model = new HashMap<String, Object>( );

        model.put( MARK_HTML_SECTION, htmlSection );
        model.put( MARK_WEBAPP_URL, strBaseUrl );
        model.put( MARK_LOCALE, locale );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_FREE_HTML_CONFIGURATION, locale, model );

        return template.getHtml( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveConfiguration( Map<String, String[]> mapParameters, NewsletterSection newsletterSection,
            AdminUser user, Locale locale )
    {
        String strContent = NewsletterUtils.getStringFromStringArray( mapParameters.get( PARAMETER_CONTENT ) );
        if ( StringUtils.isNotEmpty( strContent ) )
        {
            FreeHtmlSection section = FreeHtmlSectionHome.findByPrimaryKey( newsletterSection.getId( ), getPlugin( ) );
            section.setHtmlContent( strContent );
            FreeHtmlSectionHome.updateFreeHtmlSection( section, getPlugin( ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createNewsletterSection( NewsletterSection newsletterSection, AdminUser user, Locale locale )
    {
        FreeHtmlSection freeHtmlSection = new FreeHtmlSection( );
        freeHtmlSection.setId( newsletterSection.getId( ) );
        freeHtmlSection.setHtmlContent( StringUtils.EMPTY );
        FreeHtmlSectionHome.insertFreeHtmlSection( freeHtmlSection, getPlugin( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeNewsletterSection( int nNewsletterSectionId )
    {
        FreeHtmlSectionHome.removeFreeHtmlSection( nNewsletterSectionId, getPlugin( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHtmlContent( NewsletterSection newsletterSection, AdminUser user, Locale locale )
    {
        FreeHtmlSection freeHtmlSection = FreeHtmlSectionHome
                .findByPrimaryKey( newsletterSection.getId( ), getPlugin( ) );
        return freeHtmlSection.getHtmlContent( );
    }

    /**
     * Get the instance of the newsletter plugin
     * @return the newsletter plugin
     */
    private Plugin getPlugin( )
    {
        if ( _plugin == null )
        {
            _plugin = PluginService.getPlugin( NewsletterPlugin.PLUGIN_NAME );
        }
        return _plugin;
    }

}
