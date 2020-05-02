import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';

import { config } from './websocket.config';
import { WebSocketConfig } from './websocket.interfaces';
import {WebsocketService} from "../core/services/websocket.service";


@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [],
  providers: [
    WebsocketService
  ]
})
export class WebsocketModule {
  public static config(wsConfig: WebSocketConfig): ModuleWithProviders {
    return {
      ngModule: WebsocketModule,
      providers: [{ provide: config, useValue: wsConfig }]
    };
  }
}
