package com.expedia.risk.report.generator.model;

public class ConfluenceRequest
{
    private String id;
    private String title;
    private String type;
    private Space space;
    private Body body;

    private Version version;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Space getSpace()
    {
        return space;
    }

    public void setSpace(Space space)
    {
        this.space = space;
    }

    public Body getBody()
    {
        return body;
    }

    public void setBody(Body body)
    {
        this.body = body;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Version getVersion()
    {
        return version;
    }

    public void setVersion(Version version)
    {
        this.version = version;
    }
}
