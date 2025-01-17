/*
 * main.cpp
 * 
 * Copyright (C) 2016 Dominic S. Meiser <meiserdo@web.de>
 * 
 * This work is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License, or any later
 * version.
 * 
 * This work is distributed in the hope that it will be useful, but without
 * any warranty; without even the implied warranty of merchantability or
 * fitness for a particular purpose. See version 2 and version 3 of the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

#include <QtGlobal>
#if (QT_VERSION < QT_VERSION_CHECK(5, 5, 0))
#  error Qt >= 5.5.0 is required
#endif

#include "mainwindow.h"
#include <QApplication>

int main(int argc, char *argv[])
{
	QApplication a(argc, argv);
	
	MainWindow w;
	w.show();
	
	return a.exec();
}
