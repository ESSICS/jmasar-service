/*
 * Copyright (C) 2018 European Spallation Source ERIC.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package se.esss.ics.masar.web.controllers;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

/**
 * @author georgweiss
 * Created 5 Oct 2018
 */
@RestController
public class TestController extends BaseController {

	@ApiOperation(value = "Test broadcast addresses", produces = JSON)
	@GetMapping("/test/broadcastaddress")
	public String[] getBroadcastAddresses() {
		Enumeration nets;
		try {
			nets = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException se) {
			// fallback
			return toString(new InetSocketAddress[] { new InetSocketAddress("255.255.255.255", 5064) });
		}

		ArrayList list = new ArrayList(10);

		while (nets.hasMoreElements())
		{
			NetworkInterface net = (NetworkInterface)nets.nextElement();
			try
			{
				if (net.isUp())
				{
					List interfaceAddresses = net.getInterfaceAddresses();
					if (interfaceAddresses != null)
					{
						Iterator iter = interfaceAddresses.iterator();
						while (iter.hasNext())
						{
							InterfaceAddress addr = (InterfaceAddress)iter.next();
							if (addr.getBroadcast() != null)
							{
								InetSocketAddress isa = new InetSocketAddress(addr.getBroadcast(), 5064);
								if (!list.contains(isa))
									list.add(isa);
							}
						}
					}
				}
			} catch (Throwable th) {
				// some methods throw exceptions, some return null (and they shouldn't)
				// noop, skip that interface
			}
		}
		
		// fallback to loop
		if (list.size() == 0)
			list.add(new InetSocketAddress(InetAddress.getLoopbackAddress(), 5064));
		
		InetSocketAddress[] retVal = new InetSocketAddress[list.size()];
		list.toArray(retVal);
		return toString(retVal);
	}
	
	private String[] toString(InetSocketAddress[] addresses) {
		String[] s = new String[addresses.length];
		for(int i = 0; i < addresses.length; i++) {
			s[i] = addresses[i].getAddress().getHostAddress();
		}
		
		return s;
	}
}
