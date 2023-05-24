package mekanism.common;

import static mekanism.common.Mekanism.OSMIUMSELECT;

public enum Resource
{
	IRON("Iron"),
	GOLD("Gold"),
	OSMIUM(OSMIUMSELECT),
	COPPER("Copper"),
	TIN("Tin"),
	SILVER("Silver"),
	LEAD("Lead");

	private String name;

	private Resource(String s)
	{
		name = s;
	}

	public static Resource getFromName(String s)
	{
		for(Resource r : values())
		{
			if(r.name.toLowerCase().equals(s.toLowerCase()))
			{
				return r;
			}
		}

		return null;
	}

	public String getName()
	{
		return name;
	}
}
